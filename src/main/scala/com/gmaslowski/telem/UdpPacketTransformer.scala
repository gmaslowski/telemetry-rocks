package com.gmaslowski.telem

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import akka.util.ByteString

object UdpPacketTransformer {
  def props(webSocketHandler: ActorRef) = Props(classOf[UdpPacketTransformer], webSocketHandler)

  val floatFromPacket: (ByteString, Int, Int) => Float = _.slice(_, _).reverse.asByteBuffer.getFloat
  val byteFromPacket: (ByteString, Int) => Byte = _.apply(_)
}

class UdpPacketTransformer(val webSocketHandler: ActorRef) extends Actor with ActorLogging {
  override def receive = {
    case data: ByteString =>

      val speed = UdpPacketTransformer.floatFromPacket(data, 28, 32)
      val time = UdpPacketTransformer.floatFromPacket(data, 0, 4)
      val revs = UdpPacketTransformer.byteFromPacket(data, 267)
      val pitSpeedLimit = UdpPacketTransformer.byteFromPacket(data, 262)
      val limiterInfo = UdpPacketTransformer.byteFromPacket(data, 261)

      // log.info(s"current info ${speed} ${time} ${revs} ${pitSpeedLimit} ${limiterInfo}")

      webSocketHandler ! s"${(speed * 3.6).toInt}"
  }
}