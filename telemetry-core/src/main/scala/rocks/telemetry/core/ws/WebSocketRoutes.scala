package rocks.telemetry.core.ws

import akka.http.scaladsl.server.Directives._
import rocks.telemetry.core.Akka
import rocks.telemetry.core.ws.agent.AgentWebSocketHandler.handleAgentWebSocket
import rocks.telemetry.core.ws.frontend.FrontendWebSocketHandler.handleFrontendWebSocket

object WebSocketRoutes extends Akka.AkkaThings {

  val route =
    get {
      path("agent" / "ws") {
        handleWebSocketMessages(handleAgentWebSocket)
      } ~
        path("frontend" / "ws") {
          handleWebSocketMessages(handleFrontendWebSocket)
        }
    }
}
