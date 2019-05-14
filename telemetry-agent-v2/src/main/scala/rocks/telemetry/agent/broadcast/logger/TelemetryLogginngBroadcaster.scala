package rocks.telemetry.agent.broadcast.logger

import akka.actor.{Actor, ActorLogging, Props}
import rocks.telemetry.agent.broadcast.TelemetryBroadcastListener
import rocks.telemetry.agent.model.api.{Engine, LapInfo}

object TelemetryLogginngBroadcaster {

  def props = Props(classOf[TelemetryLogginngBroadcaster])
}

class TelemetryLogginngBroadcaster
  extends Actor
    with ActorLogging
    with TelemetryBroadcastListener {

  override def receive = {
    case lapInfo: LapInfo => log.info(lapInfo.asJson.replaceAll("\\s", ""))
    case engine: Engine => log.info(engine.asJson.replaceAll("\\s", ""))
  }
}
