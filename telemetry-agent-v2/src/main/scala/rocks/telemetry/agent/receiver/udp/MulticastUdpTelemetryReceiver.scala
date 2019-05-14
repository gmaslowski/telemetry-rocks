package rocks.telemetry.agent.receiver.udp

import java.net._
import java.nio.channels.DatagramChannel

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import akka.io.Inet.{DatagramChannelCreator, SocketOptionV2}
import akka.io.{IO, Udp}

object MulticastUdpTelemetryReceiver {
  def props = Props(classOf[MulticastUdpTelemetryReceiver])
}

final case class Inet4ProtocolFamily() extends DatagramChannelCreator {
  override def create() = {
    DatagramChannel.open(StandardProtocolFamily.INET)
  }
}

final case class MulticastGroup(address: String, interface: String) extends SocketOptionV2 {
  override def afterBind(s: DatagramSocket): Unit = {
    val group = InetAddress.getByName(address)
    val networkInterface = NetworkInterface.getByName(interface)
    s.getChannel.join(group, networkInterface)
  }
}

class MulticastUdpTelemetryReceiver
  extends Actor
    with ActorLogging {

  import context.system

  val opts = List(Inet4ProtocolFamily(), MulticastGroup("239.255.255.255", "lo"))
  IO(Udp) ! Udp.Bind(self, new InetSocketAddress(20201), opts)


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
