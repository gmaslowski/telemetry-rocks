package com.gmaslowski.telem

import java.net.InetSocketAddress

import akka.actor.{Actor, ActorLogging, ActorRef, Props, actorRef2Scala}
import akka.io.{IO, Udp}

object UdpTelemetryReceiver {
  def props(transformer: ActorRef, host: String, port: Int) = Props(classOf[UdpTelemetryReceiver], transformer, host, port)
}

class UdpTelemetryReceiver(val transformer: ActorRef, val host: String, val port: Int) extends Actor with ActorLogging {

  import context.system

  val udpAddress = new InetSocketAddress(host, port)

  IO(Udp) ! Udp.Bind(self, udpAddress)

  def receive = {
    case Udp.Bound(local) =>
      context.become(ready(sender()))
  }

  def ready(socket: ActorRef): Receive = {
    case Udp.Received(data, _) =>
      transformer ! data
    case Udp.Unbind => socket ! Udp.Unbind
    case Udp.Unbound => context.stop(self)
  }
}

