package com.gmaslowski.telemetry

import akka.actor.{Actor, ActorLogging, Props}
import akka.util.ByteString
import com.gmaslowski.telemetry.ByteStringReader._

object TelemetryUnmarshaller {
  def props = Props(classOf[TelemetryUnmarshaller])
}

class TelemetryUnmarshaller extends Actor with ActorLogging {
  override def receive = {
    case packet: ByteString => {

      val speed = packet.m_speed
      val engineRate = packet.m_engineRate
      val gear = packet.m_gear
      val idleRevs = packet.m_idle_rpm
      val maxRevs = packet.m_max_rpm

      // webSocketHandler ! CarData((speed * 3.6).round.toInt, gear.toInt - 1, Revs(engineRate.toInt, idleRevs.toInt, maxRevs.toInt))
    }
  }
}
