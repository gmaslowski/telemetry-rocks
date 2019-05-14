package rocks.telemetry.agent.broadcast.telnet

import java.lang.System.lineSeparator
import java.net.InetSocketAddress

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import akka.io.{IO, Udp}
import akka.util.ByteString
import rocks.telemetry.agent.broadcast.TelemetryBroadcastListener
import rocks.telemetry.agent.model.api.Engine

object TelemetryTelnetBroadcaster {
  def props(host: String, port: Int) = Props(classOf[TelemetryTelnetBroadcaster], new InetSocketAddress(host, port))
}

class TelemetryTelnetBroadcaster(remote: InetSocketAddress)
  extends Actor
    with ActorLogging
    with TelemetryBroadcastListener {

  import context.system

  IO(Udp) ! Udp.SimpleSender


  override def receive = {
    case Udp.SimpleSenderReady => context.become(ready(sender()))
  }

  def ready(send: ActorRef): Receive = {
    case engine: Engine => send ! Udp.Send(formatTelnetMessage(engine), remote)
  }

  private def formatTelnetMessage(engine: Engine) = ByteString(s"${engine.revPercentage}${lineSeparator()}")
}
