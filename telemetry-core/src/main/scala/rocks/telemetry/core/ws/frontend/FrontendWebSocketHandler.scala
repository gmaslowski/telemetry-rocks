package rocks.telemetry.core.ws.frontend

import akka.NotUsed
import akka.actor.PoisonPill
import akka.http.scaladsl.model.ws.{Message, TextMessage}
import akka.stream.OverflowStrategy
import akka.stream.scaladsl.{Flow, Sink, Source}
import akka.util.Timeout
import rocks.telemetry.core.ws.frontend.FrontendWebSocketConnection.OutgoingMessage
import rocks.telemetry.core.ws.frontend.api.JsonSupport
import rocks.telemetry.core.{Akka, FormulaOneTelemetry}

import scala.concurrent.ExecutionContext
import scala.concurrent.duration._

object FrontendWebSocketHandler extends Akka.AkkaThings with JsonSupport {

  def handleFrontendWebSocket: Flow[Message, Message, NotUsed] = {

    implicit val ec: ExecutionContext = system.dispatcher
    implicit val timeout: Timeout = 5 seconds

    val wsConnection = system.actorOf(FrontendWebSocketConnection.props(FormulaOneTelemetry.wsHandler))

    val incomingMessages: Sink[Message, NotUsed] = Flow[Message]
      .map {
        case TextMessage.Strict(text) => FrontendWebSocketConnection.AuthenticateRequest(text)
      }
      .to(Sink.actorRef[FrontendWebSocketConnection.AuthenticateRequest](wsConnection, PoisonPill))

    val outgoingMessages: Source[Message, NotUsed] = Source.actorRef[OutgoingMessage](10, OverflowStrategy.fail)
      .mapMaterializedValue { outActor =>
        // give the user actor a way to send messages out
        wsConnection ! FrontendWebSocketConnection.ConnectRequest(outActor)
        NotUsed
      }
      .map((msg: OutgoingMessage) => TextMessage(write(msg.json).compactPrint))

    Flow.fromSinkAndSource(incomingMessages, outgoingMessages)
  }
}
