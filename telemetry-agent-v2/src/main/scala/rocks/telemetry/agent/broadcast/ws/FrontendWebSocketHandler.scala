package rocks.telemetry.agent.broadcast.ws

import akka.NotUsed
import akka.actor.{ActorRef, ActorSystem, PoisonPill}
import akka.http.scaladsl.model.ws.{Message, TextMessage}
import akka.stream.OverflowStrategy
import akka.stream.scaladsl.{Flow, Sink, Source}
import rocks.telemetry.agent.broadcast.ws.FrontendWebSocketConnection.OutgoingMessage

object FrontendWebSocketHandler {

  def handleFrontendWebSocket(wsSender: ActorRef)(implicit system: ActorSystem): Flow[Message, Message, NotUsed] = {
    val wsConnection = system.actorOf(FrontendWebSocketConnection.props(wsSender))

    val incomingMessages: Sink[Message, NotUsed] = Flow[Message]
      .map {
        case TextMessage.Strict(text) => FrontendWebSocketConnection.IncomingMessage()
      }
      .to(Sink.actorRef[FrontendWebSocketConnection.IncomingMessage](wsConnection, PoisonPill))

    val outgoingMessages: Source[Message, NotUsed] = Source.actorRef[OutgoingMessage](10, OverflowStrategy.fail)
      .mapMaterializedValue { outActor =>
        wsConnection ! FrontendWebSocketConnection.Connected(outActor)
        NotUsed
      }
      .map((outgoing: OutgoingMessage) => TextMessage(outgoing.msg))

    Flow.fromSinkAndSource(incomingMessages, outgoingMessages)
  }
}
