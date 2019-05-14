package rocks.telemetry.agent.model.api

import akka.util.ByteString
import ByteStringReader._

trait TelemetryApi {
  def asJson: String
}

trait TelemetryApiObject {
  def fromPacket(packet: ByteString): TelemetryApi
}

object Engine extends TelemetryApiObject {
  override def fromPacket(packet: ByteString): TelemetryApi = Engine(packet.m_engineRate.toInt, packet.m_idle_rpm.toInt, packet.m_max_rpm.toInt)
}

case class Engine(currentRevs: Int, idleRevs: Int, maxRevs: Int) extends TelemetryApi {

  override def asJson: String = s"""
      {
       "currentRevs": ${this.currentRevs},
       "idleRevs": ${this.idleRevs},
       "maxRevs": ${this.maxRevs},
       "revPercentage": ${this.revPercentage}
      }
    """

  def revPercentage = ((currentRevs.toDouble / maxRevs.toDouble) * 100).intValue()

}

object LapInfo extends TelemetryApiObject {
  override def fromPacket(packet: ByteString): LapInfo = LapInfo(packet.m_lapTime,
    packet.m_sector1_time,
    packet.m_sector2_time,
    packet.m_sector1_time,
    packet.m_lap.toInt,
    packet.m_currentLapInvalid != 0)
}

case class LapInfo(lapTime: Float, s1Time: Float, s2Time: Float, s3Time: Float, lapNumber: Integer, invalidated: Boolean) extends TelemetryApi {

  def asJson = s"""
      {
       "lapTime": ${this.lapTime},
       "s1Time": ${this.s1Time},
       "s2Time": ${this.s2Time},
       "s3Time": ${this.s3Time},
       "lapNumber": ${this.lapNumber + 1},
       "invalidated": ${this.invalidated}
      }
    """
}
