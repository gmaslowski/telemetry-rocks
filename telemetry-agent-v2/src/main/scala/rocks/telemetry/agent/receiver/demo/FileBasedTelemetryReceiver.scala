package rocks.telemetry.agent.receiver.demo

import java.nio.file.{Files, Paths}

import akka.actor.{Actor, ActorLogging, Props}
import akka.util.ByteString
import rocks.telemetry.agent.receiver.demo.FileBasedTelemetryReceiver.{PlayPackets, PlayRecordedDemo}

import scala.concurrent.duration._

object FileBasedTelemetryReceiver {
  def props = Props(classOf[FileBasedTelemetryReceiver])

  val PacketSize = 1237

  // API
  case class PlayRecordedDemo(filename: String)
  case object PlayPackets
}

class FileBasedTelemetryReceiver extends Actor with ActorLogging {

  implicit val ec = context.dispatcher

  var packetsIterator: Iterator[Array[Byte]] = Iterator.empty
  var readPackets: Array[Byte] = Array.emptyByteArray
  var fromFile = ""

  override def receive = {
    case PlayRecordedDemo(filePath) =>
      fromFile = filePath
      // todo: be extra careful; it loads all to memory
      readPackets = Files.readAllBytes(Paths.get(filePath))
      packetsIterator = readPackets.grouped(FileBasedTelemetryReceiver.PacketSize)
      context.system.scheduler.scheduleOnce(20 milliseconds, self, PlayPackets)

    case PlayPackets =>
      if (packetsIterator.hasNext) {
        context.system.eventStream.publish(ByteString(packetsIterator.next()))
        context.system.scheduler.scheduleOnce(20 milliseconds, self, PlayPackets)
      } else {
        self ! PlayRecordedDemo(fromFile)
      }
  }
}