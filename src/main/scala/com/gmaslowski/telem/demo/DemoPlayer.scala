package com.gmaslowski.telem.demo

import java.nio.file.{Files, Paths}

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import akka.util.ByteString
import com.gmaslowski.telem.demo.DemoPlayer.{PlayPackets, PlayRecordedDemo}

object DemoPlayer {
  def props(demoDataReceiver: ActorRef) = Props(classOf[DemoPlayer], demoDataReceiver)

  val PacketSize = 1237

  // API
  case class PlayRecordedDemo(filename: String)
  case object PlayPackets
}

class DemoPlayer(demoDataReceiver: ActorRef) extends Actor with ActorLogging {

  import scala.concurrent.duration._
  implicit val ec = context.dispatcher

  var packetsIterator: Iterator[Array[Byte]] = Iterator.empty

  override def receive = {
    case PlayRecordedDemo(fromFile) =>
      // be extra careful; it loads all to memory
      val readPackets = Files.readAllBytes(Paths.get(fromFile))
      packetsIterator = readPackets.grouped(DemoPlayer.PacketSize)
      context.system.scheduler.scheduleOnce(33 milliseconds, self, PlayPackets)

    case PlayPackets =>
      if (packetsIterator.hasNext) {
        demoDataReceiver ! ByteString(packetsIterator.next())
        context.system.scheduler.scheduleOnce(33 milliseconds, self, PlayPackets)
      }
  }
}
