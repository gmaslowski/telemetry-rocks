package rocks.telemetry.core.live.model

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import akka.util.ByteString
import rocks.telemetry.core.live.model.ByteStringReader._
import rocks.telemetry.core.live.model.F1Enums._
import rocks.telemetry.core.live.model.LapInformation.LapData
import rocks.telemetry.core.live.model.TelemetryModel.{TelemetryData, TelemetryDataWithPlayer}
import rocks.telemetry.core.live.model.TelemetryReceiver.Player

object LapInformation {
  def props(player: Player) = Props(classOf[LapInformation], player)

  // api
  case class LapData(sector1: Float,
                     sector2: Float,
                     sector3: Float,
                     lap: Float,
                     track: String,
                     dataType: String = "LapData") extends TelemetryData

}

class LapInformation(val player: Player) extends Actor with ActorLogging {

  var lapData: LapData = LapData(0.0f, 0.0f, 0.0f, 0.0f, "")

  var lastS1: Float = 0.0f
  var lastS2: Float = 0.0f
  var lastLap: Float = 0.0f

  override def receive = {
    case (packet: ByteString, changesHandler: ActorRef) =>

      var s1Changed = false
      var s2Changed = false
      var s3Changed = false

      if (lastS1 != packet.m_sector1_time) {
        s1Changed = true
        lastS1 = packet.m_sector1_time
      }

      if (lastS2 != packet.m_sector2_time) {
        s2Changed = true
        lastS2 = packet.m_sector2_time
      }

      if (lastLap != packet.m_last_lap_time) {
        s3Changed = true
        lastLap = packet.m_last_lap_time
      }

      if (s1Changed && !s3Changed) {
        lapData = LapData(
          packet.m_sector1_time,
          0.0f,
          0.0f,
          0.0f,
          packet.m_track_number.toInt.asTrack)
      }

      if (s2Changed && !s3Changed) {
        lapData = LapData(
          packet.m_sector1_time,
          packet.m_sector2_time,
          0.0f,
          0.0f,
          packet.m_track_number.toInt.asTrack)
      }

      if (s3Changed) {
        lapData = LapData(
          lapData.sector1,
          lapData.sector2,
          packet.m_last_lap_time - lapData.sector1 - lapData.sector2,
          packet.m_last_lap_time,
          packet.m_track_number.toInt.asTrack)
      }

      if (s1Changed || s2Changed || s3Changed) {
        changesHandler ! TelemetryDataWithPlayer(player, lapData)
      }
  }
}
