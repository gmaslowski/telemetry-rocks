package com.gmaslowski.telem

import javax.inject.{Inject, Singleton}

import akka.actor._
import com.gmaslowski.telem.demo.DemoRecorder
import com.typesafe.config.ConfigFactory


@Singleton
class Bootstrap @Inject()(system: ActorSystem) {
  system.actorOf(FormulaOneTelemetry.props,"f1")
}

object FormulaOneTelemetry {
  def props = Props(classOf[FormulaOneTelemetry])
  var webSocketHandler: Option[ActorRef] = Option.empty
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
    } else {
      // normal start
      val wsHandler = context.actorOf(WebSocketHandler.props, "websocket-handler")
      // todo: not the way to do it !
      FormulaOneTelemetry.webSocketHandler = Option(wsHandler)
      val packetTransformer = context.actorOf(UdpPacketTransformer.props(wsHandler))
      val packetReceiver = context.actorOf(UdpTelemetryReceiver.props(packetTransformer, host, port))
    }
  }

  override def receive = {
    case _ =>
  }
}
