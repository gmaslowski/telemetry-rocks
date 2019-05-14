package rocks.telemetry.agent.receiver.udp

import java.net._

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import akka.io.{IO, Udp}

object DirectUdpTelemetryReceiver {
  def props = Props(classOf[DirectUdpTelemetryReceiver])
}

class DirectUdpTelemetryReceiver
  extends Actor
    with ActorLogging {

  import context.system

  val udpAddress = new InetSocketAddress("0.0.0.0", 20777)

  IO(Udp) ! Udp.Bind(self, udpAddress)

  def receive = {
    case Udp.Bound(_) => context.become(ready(sender()))
  }

  def ready(socket: ActorRef): Receive = {

    case Udp.Received(data, _) =>
      context.system.eventStream.publish(data)

    case Udp.Unbind =>
      socket ! Udp.Unbind

    case Udp.Unbound =>
      context.stop(self)
  }
}
