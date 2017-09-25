package com.gmaslowski.telemetry

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import akka.util.ByteString
import com.gmaslowski.telemetry.ByteStringReader._
import com.gmaslowski.telemetry.ws.api.TelemetryWebSocketApi.{CarData, Revs}

object TelemetryReceiver {
  def props(webSocketHandler: ActorRef) = Props(classOf[TelemetryReceiver], webSocketHandler)
}

class TelemetryReceiver(val webSocketHandler: ActorRef) extends Actor with ActorLogging {

  val telemetryUnmarshaller = context.actorOf(TelemetryUnmarshaller.props)

  override def receive = {
    case data: ByteString =>
      telemetryUnmarshaller forward data

      val speed = data.floatFromPacket(8)
      val engineRate = data.floatFromPacket(38)
      val gear = data.floatFromPacket(34)
      val idleRevs = data.floatFromPacket(65)
      val maxRevs = data.floatFromPacket(64)

      webSocketHandler ! CarData((speed * 3.6).round.toInt, gear.toInt - 1, Revs(engineRate.toInt, 0, maxRevs.toInt))
  }
}