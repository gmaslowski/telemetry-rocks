package com.gmaslowski.telemetry.demo

import java.nio.file.{Files, Paths}

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import akka.util.ByteString
import com.gmaslowski.telemetry.demo.TelemetryDemoPlayer.{PlayPackets, PlayRecordedDemo}

import scala.concurrent.duration._

object TelemetryDemoPlayer {
  def props(demoDataReceiver: ActorRef) = Props(classOf[TelemetryDemoPlayer], demoDataReceiver)

  val PacketSize = 1237

  // API
  case class PlayRecordedDemo(filename: String)
  case object PlayPackets
}

class TelemetryDemoPlayer(demoDataReceiver: ActorRef) extends Actor with ActorLogging {

  implicit val ec = context.dispatcher

  var packetsIterator: Iterator[Array[Byte]] = Iterator.empty

  override def receive = {
    case PlayRecordedDemo(fromFile) =>
      // todo: be extra careful; it loads all to memory
      val readPackets = Files.readAllBytes(Paths.get(fromFile))
      packetsIterator = readPackets.grouped(TelemetryDemoPlayer.PacketSize)
      context.system.scheduler.scheduleOnce(33 milliseconds, self, PlayPackets)

    case PlayPackets =>
      if (packetsIterator.hasNext) {
        demoDataReceiver ! ByteString(packetsIterator.next())
        context.system.scheduler.scheduleOnce(33 milliseconds, self, PlayPackets)
      }
  }
}
