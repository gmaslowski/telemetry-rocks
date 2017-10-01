package com.gmaslowski.telemetry.live.model

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import akka.util.ByteString
import com.gmaslowski.telemetry.ByteStringReader._
import com.gmaslowski.telemetry.live.model.LiveInformation.LiveData
import com.gmaslowski.telemetry.live.model.TelemetryModel.TelemetryData


object LiveInformation {
  def props = Props(classOf[LiveInformation])

  // api
  case class LiveData(speed: Int,
                      gear: Int,
                      revs: Int,
                      throttle: Float,
                      break: Float,
                      x: Float,
                      y: Float,
                      dataType: String = "LiveData") extends TelemetryData
  val EmptyLiveData = LiveData(0, 0, 0, 0.0f, 0.0f, 0.0f, 0.0f)
}

class LiveInformation extends Actor with ActorLogging {
  override def receive = {
    case (packet: ByteString, receiver: ActorRef) =>

      receiver ! LiveData(
        (packet.m_speed * 3.6).round.toInt,
        packet.m_gear.toInt - 1,
        packet.m_engineRate.toInt,
        packet.m_throttle,
        packet.m_brake,
        packet.m_x / 4,
        packet.m_z / 4
      )
  }
}
