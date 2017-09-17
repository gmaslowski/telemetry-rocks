package com.gmaslowski.telem

import java.net.InetSocketAddress

import akka.actor.{Actor, ActorLogging, ActorRef, Props, actorRef2Scala}
import akka.io.{IO, Udp}

object UdpTelemetryReceiver {
  def props(transformer: ActorRef) = Props(classOf[UdpTelemetryReceiver], transformer)
}

class UdpTelemetryReceiver(val transfomer: ActorRef) extends Actor with ActorLogging {

  import context.system

  val udpAddress = new InetSocketAddress("0.0.0.0", 20777)

  IO(Udp) ! Udp.Bind(self, udpAddress)

  def receive = {
    case Udp.Bound(local) =>
      context.become(ready(sender()))
  }

  def ready(socket: ActorRef): Receive = {
    case Udp.Received(data, _) =>
      transfomer ! data
    case Udp.Unbind => socket ! Udp.Unbind
    case Udp.Unbound => context.stop(self)
  }
}

