package rocks.telemetry.agent.broadcast

import akka.actor.{ActorRef, ActorSystem}
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives.{get, handleWebSocketMessages, path}
import akka.stream.ActorMaterializer
import rocks.telemetry.agent.broadcast.audio.TelemetryAudioBroadcaster
import rocks.telemetry.agent.broadcast.logger.TelemetryLogginngBroadcaster
import rocks.telemetry.agent.broadcast.telnet.TelemetryTelnetBroadcaster
import rocks.telemetry.agent.broadcast.ws.FrontendWebSocketHandler.handleFrontendWebSocket
import rocks.telemetry.agent.broadcast.ws.{TelemetryWebsocketBroadcaster, WebSocketSender}

object TelemetryBroadcastingComponents {

  trait Requires {
    implicit val system: ActorSystem
    implicit val materializer: ActorMaterializer
  }

  trait Provides

  trait WebSocket
    extends Requires
      with Provides {

    private val wsSender: ActorRef = system.actorOf(WebSocketSender.props)
    system.actorOf(TelemetryWebsocketBroadcaster.props(wsSender))

    private val route =
      get {
        path("ws") {
          handleWebSocketMessages(handleFrontendWebSocket(wsSender))
        }
      }

    Http().bindAndHandle(route, "0.0.0.0", 8080)
  }

  trait Logger
    extends Requires
      with Provides {
    system.actorOf(TelemetryLogginngBroadcaster.props)
  }

  trait Audio
    extends Requires
      with Provides {
    system.actorOf(TelemetryAudioBroadcaster.props)
  }

  trait Telnet
    extends Requires
      with Provides {
    system.actorOf(TelemetryTelnetBroadcaster.props("127.0.0.1", 20778))
  }
}
