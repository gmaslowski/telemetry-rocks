package com.gmaslowski.telemetry.udp

import java.net.InetSocketAddress

import akka.actor.{Actor, ActorLogging, ActorRef, Props, actorRef2Scala}
import akka.io.{IO, Udp}
import akka.pattern.{ask, pipe}
import akka.util.Timeout
import com.gmaslowski.telemetry.udp.UdpTelemetryPacketValidator.{InvalidPacket, ValidPacket, Validate}
import com.typesafe.config.Config

import scala.concurrent.duration._

object UdpTelemetryPacketReceiver {
  def props(transformer: ActorRef, config: Config) = Props(classOf[UdpTelemetryPacketReceiver], transformer, config)
}

class UdpTelemetryPacketReceiver(val telemetryReceiver: ActorRef, val config: Config) extends Actor with ActorLogging {

  import context.system
  val udpAddress = new InetSocketAddress(config.getString("udp.listen.host"), config.getInt("udp.listen.port"))

  IO(Udp) ! Udp.Bind(self, udpAddress)

  def receive = {
    case Udp.Bound(_) =>
      context.become(ready(sender()))
  }

  def ready(socket: ActorRef): Receive = {

    case Udp.Received(data, _) =>
      implicit val timeout: Timeout = 100 milliseconds
      implicit val ec = context.dispatcher
      context.system.actorOf(UdpTelemetryPacketValidator.props) ? Validate(data) pipeTo self

    case InvalidPacket(data) =>
      log.warning("Dropping invalid packet.")

    case ValidPacket(data) =>
      telemetryReceiver ! data

    case Udp.Unbind =>
      socket ! Udp.Unbind

    case Udp.Unbound =>
      context.stop(self)
  }
}
