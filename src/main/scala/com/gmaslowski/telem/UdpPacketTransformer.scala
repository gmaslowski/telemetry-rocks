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
      // val revs = UdpPacketTransformer.byteFromPacket(data, 332) // rev leds!
      // val engineRate = UdpPacketTransformer.floatFromPacket(data, 158, 162)

      webSocketHandler ! s"${(speed * 3.6).round}"
  }
}