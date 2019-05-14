package rocks.telemetry.agent.broadcast

import akka.actor.Actor
import rocks.telemetry.agent.model.api.TelemetryApi

trait TelemetryBroadcastListener extends Actor {

  override def preStart = context.system.eventStream.subscribe(self, classOf[TelemetryApi])
}
