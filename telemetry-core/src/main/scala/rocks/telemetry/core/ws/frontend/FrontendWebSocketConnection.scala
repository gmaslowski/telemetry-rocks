package rocks.telemetry.core.ws.frontend

import akka.actor.{ActorRef, FSM, Props}
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.HttpMethods.GET
import akka.http.scaladsl.model.{HttpRequest, HttpResponse}
import akka.util.ByteString
import rocks.telemetry.core.live.model.TelemetryReceiver.Player
import rocks.telemetry.core.ws.frontend.FrontendWebSocketConnection._
import rocks.telemetry.core.ws.frontend.WebSocketHandler.RegisterWsClient
import rocks.telemetry.core.ws.frontend.api.TelemetryWebSocketApi.JsonApi
import rocks.telemetry.core.{Akka, FormulaOneTelemetry}
import spray.json._
import DefaultJsonProtocol._

import scala.concurrent.ExecutionContext

object FrontendWebSocketConnection {

  val telemetryAuthUrl = FormulaOneTelemetry.config.getString("telemetry.auth.url") + "/user"

  def props(wsHandler: ActorRef) = Props(classOf[FrontendWebSocketConnection], wsHandler)

  // api
  case class AuthenticateRequest(token: String)
  case class ConnectRequest(outgoing: ActorRef)
  case class IncomingMessage(json: JsonApi)
  case class OutgoingMessage(json: JsonApi)

  // states
  sealed trait State
  case object InactiveState extends State
  case object ConnectedState extends State

  // data
  sealed trait Data
  case object Uninitialized extends Data
  final case class WithReceiver(outgoing: ActorRef) extends Data

}

class FrontendWebSocketConnection(wsHandler: ActorRef) extends FSM[State, Data] with Akka.AkkaThings {

  startWith(InactiveState, Uninitialized)

  when(InactiveState) {
    case Event(ConnectRequest(outgoing), Uninitialized) =>
      log.info("Received connection request.")
      stay using WithReceiver(outgoing)

    case Event(AuthenticateRequest(token), WithReceiver(outgoing)) =>
      log.info("Received authentication request.")
      import akka.pattern.pipe
      implicit val ec: ExecutionContext = context.dispatcher

      Http()
        .singleRequest(
          HttpRequest(uri = s"$telemetryAuthUrl?access_token=$token")
            .withMethod(GET)
        ) pipeTo self

      stay using WithReceiver(outgoing)

    case Event(httpResponse: HttpResponse, WithReceiver(outgoing)) =>
      log.info("Authenticated?")
      implicit val ec: ExecutionContext = context.dispatcher

      httpResponse.entity.dataBytes.runFold(ByteString(""))(_ ++ _).foreach { body =>
        val email = body.utf8String.parseJson.asJsObject.fields("principal").convertTo[String]
        wsHandler ! RegisterWsClient(Player(email), self)
      }

      goto(ConnectedState) using WithReceiver(outgoing)
  }

  when(ConnectedState) {

    case Event(json: JsonApi, WithReceiver(outgoing)) =>
      outgoing ! OutgoingMessage(json)

      stay using WithReceiver(outgoing)
  }

  initialize()
}