package rocks.telemetry.core.ws.agent

import akka.NotUsed
import akka.actor.PoisonPill
import akka.http.scaladsl.model.ws.{BinaryMessage, Message, TextMessage}
import akka.stream.OverflowStrategy
import akka.stream.scaladsl.{Flow, Keep, Sink, Source}
import rocks.telemetry.core.Akka.AkkaThings
import rocks.telemetry.core.ws.agent.AgentWebSocketConnection.Connected

object AgentWebSocketHandler extends AkkaThings {

  def handleAgentWebSocket: Flow[Message, Message, NotUsed] = {
    val wsConnection = system.actorOf(AgentWebSocketConnection.props(system.actorOf(AuthenticationVerifier.props)))

    val incomingMessages: Sink[Message, NotUsed] = Flow[Message]
      .map {
        case BinaryMessage.Strict(binaryPacket) => AgentWebSocketConnection.PacketMessage(binaryPacket)
        case TextMessage.Strict(emailWithApiKey) =>
          val strings = emailWithApiKey.split(":")
          AgentWebSocketConnection.AuthenticationRequest(strings(0), strings(1))
      }
      .to(Sink.actorRef[AgentWebSocketConnection.IncomingMessage](wsConnection, PoisonPill))

    val outgoingMessages: Source[Message, NotUsed] = Source.actorRef[String](10, OverflowStrategy.fail)
      .mapMaterializedValue {
        outActor => wsConnection ! Connected(outActor)
          NotUsed
      }
      .map((msg: String) => TextMessage(msg))

    Flow.fromSinkAndSource(incomingMessages, outgoingMessages)
  }
}
