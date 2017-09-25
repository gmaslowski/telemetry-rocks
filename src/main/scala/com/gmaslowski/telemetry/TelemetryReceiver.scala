package com.gmaslowski.telemetry

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import akka.util.ByteString
import com.gmaslowski.telemetry.ByteStringReader._
import com.gmaslowski.telemetry.F1Enums._
import com.gmaslowski.telemetry.ws.api.TelemetryWebSocketApi.{CarData, Revs}

object TelemetryReceiver {
  def props(webSocketHandler: ActorRef) = Props(classOf[TelemetryReceiver], webSocketHandler)
}

class TelemetryReceiver(val webSocketHandler: ActorRef) extends Actor with ActorLogging {

  val telemetryUnmarshaller = context.actorOf(TelemetryUnmarshaller.props)

  override def receive = {
    case packet: ByteString =>
      telemetryUnmarshaller forward packet

      webSocketHandler ! CarData(
        (packet.m_speed * 3.6).round.toInt,
        packet.m_gear.toInt - 1,
        packet.m_tyre_compound.asTyre,
        packet.m_team_info.toInt.asTeam,
        packet.m_sector1_time,
        packet.m_sector2_time,
        packet.m_last_lap_time,
        Revs(
          packet.m_engineRate.toInt,
          0,
          packet.m_max_rpm.toInt
        )
      )
  }
}