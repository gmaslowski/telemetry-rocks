package rocks.telemetry.core.live.model

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import akka.util.ByteString
import rocks.telemetry.core.live.model.ByteStringReader._
import rocks.telemetry.core.live.model.LiveInformation.LiveData
import rocks.telemetry.core.live.model.TelemetryModel.{TelemetryData, TelemetryDataWithPlayer}
import rocks.telemetry.core.live.model.TelemetryReceiver.Player


object LiveInformation {
  def props(player: Player) = Props(classOf[LiveInformation], player)

  // api
  case class LiveData(speed: Int,
                      gear: Int,
                      revs: Int,
                      throttle: Float,
                      break: Float,
                      x: Float,
                      y: Float,
                      lapTime: Float,
                      dataType: String = "LiveData") extends TelemetryData

  val EmptyLiveData = LiveData(0, 0, 0, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f)
}

class LiveInformation(val player: Player) extends Actor with ActorLogging {

  override def receive = {
    case (packet: ByteString, changesHandler: ActorRef) =>

      changesHandler ! TelemetryDataWithPlayer(player,
        LiveData(
          (packet.m_speed * 3.6).round.toInt,
          packet.m_gear.toInt - 1,
          packet.m_engineRate.toInt,
          packet.m_throttle,
          packet.m_brake,
          packet.m_x / 4,
          packet.m_z / 4,
          packet.m_lapTime
        ))
  }
}
