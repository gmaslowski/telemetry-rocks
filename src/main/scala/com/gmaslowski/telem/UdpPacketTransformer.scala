package com.gmaslowski.telem

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import akka.util.ByteString
import com.gmaslowski.telem.UdpPacketTransformer.fourBytesFrom
import com.gmaslowski.telem.api.TelemetryWebSocketApi.{CarData, Revs}

object UdpPacketTransformer {
  def props(webSocketHandler: ActorRef) = Props(classOf[UdpPacketTransformer], webSocketHandler)

  val fourBytesFrom: Int => Range = idx => Range(idx * 4 - 4, idx * 4)
  val floatFromPacket: (ByteString, Range) => Float = (data, range) => data.slice(range.start, range.end).reverse.asByteBuffer.getFloat
  val byteFromPacket: (ByteString, Int) => Byte = _.apply(_)
}

class UdpPacketTransformer(val webSocketHandler: ActorRef) extends Actor with ActorLogging {
  override def receive = {
    case data: ByteString =>

      val speed = UdpPacketTransformer.floatFromPacket(data, fourBytesFrom(8)) // 8
      val engineRate = UdpPacketTransformer.floatFromPacket(data, fourBytesFrom(38)) // 38
      val gear = UdpPacketTransformer.floatFromPacket(data, fourBytesFrom(34)) // 34
      val idleRevs = UdpPacketTransformer.floatFromPacket(data, fourBytesFrom(65)) //64
      val maxRevs = UdpPacketTransformer.floatFromPacket(data, fourBytesFrom(64)) //65

      webSocketHandler ! CarData((speed * 3.6).round.toInt, gear.toInt - 1, Revs(engineRate.toInt, idleRevs.toInt, maxRevs.toInt))
  }
}