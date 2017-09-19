package com.gmaslowski.telem

import akka.actor._
import akka.util.ByteString
import com.gmaslowski.telem.demo.DemoRecorder
import com.typesafe.config.ConfigFactory

object Bootstrap {

  def main(args: Array[String]): Unit = {
    val system = ActorSystem("f1-telemetry")
    system.actorOf(FormulaOneTelemetry.props)
  }
}

object FormulaOneTelemetry {
  def props = Props(classOf[FormulaOneTelemetry])
}

class FormulaOneTelemetry extends Actor with ActorLogging {
  override def preStart = {

    val config = ConfigFactory.defaultApplication()
    val host = config.getString("listen.host")
    val port = config.getInt("listen.port")


    if (config.getBoolean("demo.record") && config.getString("demo.filename") != null) {
      // record demo mode
      val demoRecorder = context.actorOf(DemoRecorder.props(config.getString("demo.filename")))
      val packetReceiver = context.actorOf(UdpTelemetryReceiver.props(demoRecorder, host, port))
      demoRecorder ! ByteString("sssssasdas")
    } else {
      // normal start
      val packetTransformer = context.actorOf(UdpPacketTransformer.props)
      val packetReceiver = context.actorOf(UdpTelemetryReceiver.props(packetTransformer, host, port))
    }
  }

  override def receive = {
    case _ =>
  }
}
