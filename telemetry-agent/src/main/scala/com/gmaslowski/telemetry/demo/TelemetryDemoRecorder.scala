package com.gmaslowski.telemetry.demo

import java.io.FileOutputStream
import java.nio.file.{Files, Paths}
import java.util.concurrent.TimeUnit.SECONDS

import akka.actor.{ActorLogging, FSM, Props, ReceiveTimeout}
import akka.util.ByteString
import com.gmaslowski.telemetry.demo.TelemetryDemoRecorder._

import scala.collection.immutable.Queue
import scala.concurrent.duration.Duration
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

object TelemetryDemoRecorder {
  def props(filename: String) = Props(classOf[TelemetryDemoRecorder], filename)

  // states
  sealed trait State
  case object Inactive extends State
  case object Recording extends State
  case object StoppedRecording extends State

  // data
  sealed trait Data
  case object Uninitialized extends Data
  final case class RecordedData(packetList: Queue[ByteString]) extends Data
}

class TelemetryDemoRecorder(val filename: String) extends FSM[State, Data] with ActorLogging {

  startWith(Inactive, Uninitialized)

  when(Inactive) {
    case Event(packet: ByteString, Uninitialized) =>
      goto(Recording) using RecordedData(Queue.empty.enqueue(packet))
  }

  when(Recording) {
    case Event(packet: ByteString, RecordedData(packetList)) =>
      stay using RecordedData(packetList.enqueue(packet))

    case Event(ReceiveTimeout, data: RecordedData) =>
      implicit val ec: ExecutionContext = context.dispatcher
      log.info("Didn't receive any data since last 5 seconds. Stopping demo recording.")

      Future {
        Files.delete(Paths.get(filename))
        val output = new FileOutputStream(filename, true)
        data.packetList.foreach(byteString =>{
          output.write(byteString.toArray[Byte])
        })
        output.close()
        log.info(s"File $filename created.")
      } onComplete {
        case Success(_) => log.info("Successfully saved file.")
        case Failure(t) => log.error(s"Error while saving file - ${t.getMessage}.")
      }

      goto(StoppedRecording) using data
  }

  when(StoppedRecording) {
    case Event(e, s) =>
      log.warning("Received unhandled request {} in state {}/{}", e, stateName, s)
      stay
  }

  onTransition {
    case Inactive -> Recording =>
      context.setReceiveTimeout(Duration(5, SECONDS))
    case Recording -> StoppedRecording =>
      context.setReceiveTimeout(Duration.Undefined)
  }

  initialize()
}
