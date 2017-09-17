package com.gmaslowski.telem

import akka.actor._

object DistractorBootstrap {

  def main(args: Array[String]): Unit = {
    val system = ActorSystem("f1-telemetry")
    val packetTransformer = system.actorOf(UdpPacketTransformer.props)
    system.actorOf(UdpTelemetryReceiver.props(packetTransformer))
  }
}
