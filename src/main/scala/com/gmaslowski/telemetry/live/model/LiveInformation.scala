package com.gmaslowski.telemetry.live.model

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import akka.util.ByteString
import com.gmaslowski.telemetry.ByteStringReader._
import com.gmaslowski.telemetry.ws.api.TelemetryWebSocketApi.{JsonApi, LiveData}


object LiveInformation {
  def props = Props(classOf[LiveInformation])
}

class LiveInformation extends Actor with ActorLogging {
  override def receive = {
    case (packet: ByteString, receiver: ActorRef) =>
      receiver ! LiveData(
        (packet.m_speed * 3.6).round.toInt,
        packet.m_gear.toInt - 1,
        packet.m_sector1_time,
        packet.m_sector2_time,
        packet.m_last_lap_time,
        packet.m_engineRate.toInt
      )
  }
}
