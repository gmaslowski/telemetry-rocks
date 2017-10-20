package com.gmaslowski.telemetry.live.model

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import akka.util.ByteString
import ByteStringReader._
import com.gmaslowski.telemetry.live.model.LapInformation.LapData
import com.gmaslowski.telemetry.live.model.TelemetryModel.TelemetryData

object LapInformation {
  def props = Props(classOf[LapInformation])

  // api
  case class LapData(sector1: Float,
                     sector2: Float,
                     lap: Float,
                     dataType: String = "LapData") extends TelemetryData
}

class LapInformation extends Actor with ActorLogging {

  var lapData: LapData = LapData(0.0f, 0.0f, 0.0f)

  override def receive = {
    case (packet: ByteString, receiver: ActorRef) =>
      val incomingLapData = LapData(packet.m_sector1_time, packet.m_sector2_time, packet.m_last_lap_time)

      if (lapData != incomingLapData) {
        lapData = incomingLapData
        receiver ! lapData
      }
  }
}
