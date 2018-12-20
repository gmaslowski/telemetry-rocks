package rocks.telemetry.agent.udp

import akka.actor.{Actor, ActorLogging, Props}
import akka.util.ByteString
import rocks.telemetry.agent.udp.UdpTelemetryPacketValidator.{InvalidPacket, ValidPacket, Validate}

object UdpTelemetryPacketValidator {
  def props = Props(classOf[UdpTelemetryPacketValidator])

  // api
  case class Validate(packet: ByteString)
  case class ValidPacket(data: ByteString)
  case class InvalidPacket(data: ByteString)

  // const
  val ValidPacketSize = 1289
}

class UdpTelemetryPacketValidator extends Actor with ActorLogging {

  override def receive = {
    case Validate(packet) =>
        sender() ! ValidPacket(packet)
//      if (packet.size != UdpTelemetryPacketValidator.ValidPacketSize) {
//        sender() ! InvalidPacket(packet)
//        log.warning("Invalid packet size: [{}]. Expected [{}].", packet.size, UdpTelemetryPacketValidator.ValidPacketSize)
//      }
//      else {
//      }
  }
}
