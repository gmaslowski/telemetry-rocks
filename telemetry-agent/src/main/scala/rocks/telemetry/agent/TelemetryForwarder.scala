package rocks.telemetry.agent

import akka.actor.{ActorLogging, ActorRef, FSM, PoisonPill, Props}
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.model.ws.{BinaryMessage, Message, TextMessage, WebSocketRequest}
import akka.stream.OverflowStrategy
import akka.stream.scaladsl.{Flow, Keep, Sink, Source}
import akka.util.ByteString
import akka.{Done, NotUsed}
import rocks.telemetry.agent.TelemetryForwarder._

import scala.concurrent.{ExecutionContext, Future}

object TelemetryForwarder {
  def props(wsAddress: String, email: String, apiToken: String) = Props(classOf[TelemetryForwarder], wsAddress, email, apiToken)

  // states
  sealed trait State
  case object New extends State
  case object Authorizing extends State
  case object Forwarding extends State

  // data
  sealed trait Data
  case object Uninitialized extends Data
  final case class WebSocket(webSocket: ActorRef) extends Data
}

class TelemetryForwarder(wsAddress: String, email: String, apiKey: String) extends FSM[State, Data] with ActorLogging with Akka.AkkaThings {

  implicit val ec: ExecutionContext = context.system.dispatcher

  log.info(s"Will use email=[$email] and apiKey=[$apiKey]")

  startWith(New, Uninitialized)

  when(New) {
    case Event(dropPacket: ByteString, Uninitialized) =>

      val req = WebSocketRequest(uri = wsAddress)
      val webSocketFlow = Http().webSocketClientFlow(req)

      val messageSource: Source[Message, ActorRef] =
        Source.actorRef[TextMessage.Strict](bufferSize = 20, OverflowStrategy.dropHead)

      val messageReceiver: Sink[Message, NotUsed] = Flow[Message].to(Sink.actorRef[Message](self, PoisonPill))

      val ((webSocket, upgradeResponse), _) = messageSource
        .viaMat(webSocketFlow)(Keep.both)
        .toMat(messageReceiver)(Keep.both)
        .run()

      upgradeResponse.flatMap { upgrade =>
        if (upgrade.response.status == StatusCodes.SwitchingProtocols) {
          Future.successful(Done)
        } else {
          throw new RuntimeException(s"Connection failed: ${upgrade.response.status}")
        }
      }
      webSocket ! TextMessage(s"$email:$apiKey")
      goto(Authorizing) using WebSocket(webSocket)
  }

  when(Authorizing) {
    case Event(textMessage: TextMessage, WebSocket(webSocket)) =>
      if (textMessage.getStrictText.equals("OK")) {
        log.info("Got OK, authorized.")
        goto(Forwarding) using WebSocket(webSocket)
      } else {
        stop
      }
    case Event(_: ByteString, _: WebSocket) =>
      stay
  }

  when(Forwarding) {
    case Event(packet: ByteString, WebSocket(webSocket)) =>
      webSocket ! BinaryMessage(packet)
      stay
  }

  initialize()

}
