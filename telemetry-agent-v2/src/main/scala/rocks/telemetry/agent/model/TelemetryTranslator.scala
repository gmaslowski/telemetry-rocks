package rocks.telemetry.agent.model

import akka.actor.{Actor, ActorLogging, Props}
import akka.util.ByteString
import rocks.telemetry.agent.model.api.ByteStringReader._
import rocks.telemetry.agent.model.api.{Engine, LapInfo}

import scala.concurrent.ExecutionContext

object TelemetryTranslator {

  def props = Props(classOf[TelemetryTranslator])

  val initialLapInfo = LapInfo(-1f, -1f, -1f, -1f, 0, false)
}

class TelemetryTranslator
  extends Actor
    with ActorLogging {

  implicit val ec: ExecutionContext = context.dispatcher

  var lapInfo: LapInfo = TelemetryTranslator.initialLapInfo

  override def preStart() = context.system.eventStream.subscribe(self, classOf[ByteString])

  override def receive = {
    case packet: ByteString =>
      processLapInformation(packet)
      processEngineInformation(packet)
  }

  def processEngineInformation(packet: ByteString) = {
    context.system.eventStream.publish(Engine.fromPacket(packet))
  }

  def processLapInformation(packet: ByteString) = {
    def lapFinished = lapInfo != TelemetryTranslator.initialLapInfo && lapInfo.lapNumber != packet.m_lap.toInt

    if (lapFinished) {
      context.system.eventStream.publish(lapInfo)
    }

    lapInfo = LapInfo(packet.m_lapTime,
      packet.m_sector1_time,
      packet.m_sector2_time,
      packet.m_lapTime - packet.m_sector1_time - packet.m_sector2_time,
      packet.m_lap.toInt,
      packet.m_currentLapInvalid != 0)
  }
}
