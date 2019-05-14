package rocks.telemetry.agent.receiver

import akka.actor.{ActorRef, ActorSystem}
import rocks.telemetry.agent.receiver.demo.FileBasedTelemetryReceiver
import rocks.telemetry.agent.receiver.demo.FileBasedTelemetryReceiver.PlayRecordedDemo
import rocks.telemetry.agent.receiver.udp.{DirectUdpTelemetryReceiver, MulticastUdpTelemetryReceiver}

object TelemetryReceiverComponents {

  trait Requires {
    val system: ActorSystem
  }

  trait Provides {
  }

  trait DirectUdp extends Requires with Provides {
    system.actorOf(DirectUdpTelemetryReceiver.props)
  }

  trait MulticastUdp extends Requires with Provides {
    system.actorOf(MulticastUdpTelemetryReceiver.props)
  }

  trait Demo extends Requires with Provides {

    import scala.concurrent.duration._

    implicit val ec = system.dispatcher

    val telemetryReceiver: ActorRef = system.actorOf(FileBasedTelemetryReceiver.props)
    system.scheduler.scheduleOnce(3 seconds, telemetryReceiver, PlayRecordedDemo("demo.f1t"))
  }
}
