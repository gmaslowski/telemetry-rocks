package rocks.telemetry.agent.broadcast.ws

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import rocks.telemetry.agent.broadcast.TelemetryBroadcastListener
import rocks.telemetry.agent.broadcast.ws.TelemetryWebsocketBroadcaster.Heartbeat
import rocks.telemetry.agent.model.api.{Engine, LapInfo}

import scala.concurrent.ExecutionContext

object TelemetryWebsocketBroadcaster {

  def props(interested: ActorRef) = Props(classOf[TelemetryWebsocketBroadcaster], interested)

  // API
  case object Heartbeat

}

class TelemetryWebsocketBroadcaster(interested: ActorRef)
  extends Actor
    with ActorLogging
    with TelemetryBroadcastListener {

  import scala.concurrent.duration._

  implicit val ec: ExecutionContext = context.dispatcher
  context.system.scheduler.schedule(3 seconds, 10 second, self, Heartbeat)

  override def receive = {
    case engine: Engine =>
      interested ! engine.asJson

    case lapInfo: LapInfo =>
      interested ! lapInfo.asJson

    case Heartbeat =>
      interested ! """{"isHeartbeat": true}"""
  }
}
