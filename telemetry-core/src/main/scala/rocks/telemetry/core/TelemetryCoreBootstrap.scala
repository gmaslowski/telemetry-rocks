package rocks.telemetry.core

import akka.actor.ActorRef
import akka.http.scaladsl.Http
import com.typesafe.config.ConfigFactory
import rocks.telemetry.core.live.model.ModelChangesHandler.RegisterHandler
import rocks.telemetry.core.live.model.{ModelChangesHandler, TelemetryReceiver}
import rocks.telemetry.core.pubsub.PubSubPublisher
import rocks.telemetry.core.ws.frontend.WebSocketHandler

object TelemetryCoreBootstrap extends App with Akka.AkkaThings {

  import FormulaOneTelemetry._
  import rocks.telemetry.core.ws.WebSocketRoutes._

  Http().bindAndHandle(route, config.getString("http.listen.host"), config.getInt("http.listen.port"))
}

object FormulaOneTelemetry extends Akka.AkkaThings {

  implicit val ec = system.dispatcher

  val config = ConfigFactory.defaultApplication()

  val (wsHandler, changesHandler): (ActorRef, ActorRef) = {
    val wsHandler = system.actorOf(WebSocketHandler.props, "websocket-handler")
    val pubSubPublisher = system.actorOf(PubSubPublisher.props, "pubsub-handler")
    val changesHandler = system.actorOf(ModelChangesHandler.props)
    changesHandler ! RegisterHandler(wsHandler)
    changesHandler ! RegisterHandler(pubSubPublisher)

    (wsHandler, changesHandler)
  }
}
