package com.gmaslowski.telemetry

import akka.actor.{Actor, ActorLogging, Props}
import akka.util.ByteString
import com.gmaslowski.telemetry.ByteStringReader._

object TelemetryUnmarshaller {
  def props = Props(classOf[TelemetryUnmarshaller])
}

class TelemetryUnmarshaller extends Actor with ActorLogging {
  override def receive = {
    case data: ByteString => {

      val speed = data.floatFromPacket(8)
      val engineRate = data.floatFromPacket(38)
      val gear = data.floatFromPacket(34)
      val idleRevs = data.floatFromPacket(65)
      val maxRevs = data.floatFromPacket(64)

      // webSocketHandler ! CarData((speed * 3.6).round.toInt, gear.toInt - 1, Revs(engineRate.toInt, idleRevs.toInt, maxRevs.toInt))
    }
  }
}
