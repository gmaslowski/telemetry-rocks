package com.gmaslowski.telemetry

import akka.actor._
import com.gmaslowski.telemetry.ModelChangesHandler.RegisterHandler
import com.gmaslowski.telemetry.udp.UdpTelemetryPacketReceiver
import com.gmaslowski.telemetry.demo.TelemetryDemoPlayer.PlayRecordedDemo
import com.gmaslowski.telemetry.demo.{TelemetryDemoPlayer, TelemetryDemoRecorder}
import com.gmaslowski.telemetry.ws.WebSocketHandler
import com.typesafe.config.ConfigFactory

import scala.concurrent.duration._

object FormulaOneTelemetry {
  def props = Props(classOf[FormulaOneTelemetry])

  var webSocketHandler: Option[ActorRef] = Option.empty
}

class FormulaOneTelemetry extends Actor with ActorLogging {
  override def preStart = {

    implicit val ec = context.dispatcher

    val config = ConfigFactory.defaultApplication()
    val host = config.getString("listen.host")
    val port = config.getInt("listen.port")

    if (config.getBoolean("demo.record") && config.getString("demo.filename") != null) {
      // record demo mode
      val wsHandler = context.actorOf(WebSocketHandler.props, "websocket-handler")
      FormulaOneTelemetry.webSocketHandler = Option(wsHandler)
      val changesHandler = context.actorOf(ModelChangesHandler.props)
      changesHandler ! RegisterHandler(wsHandler)

      val demoRecorder = context.actorOf(TelemetryDemoRecorder.props(config.getString("demo.filename")))
      val packetReceiver = context.actorOf(UdpTelemetryPacketReceiver.props(demoRecorder, host, port))

    } else if (config.getBoolean("demo.play") && config.getString("demo.filename") != null) {

      // play demo mode
      val wsHandler = context.actorOf(WebSocketHandler.props, "websocket-handler")
      FormulaOneTelemetry.webSocketHandler = Option(wsHandler)
      val changesHandler = context.actorOf(ModelChangesHandler.props)
      changesHandler ! RegisterHandler(wsHandler)

      val packetTransformer = context.actorOf(TelemetryReceiver.props(changesHandler))
      val demoPlayer = context.actorOf(TelemetryDemoPlayer.props(packetTransformer))

      // give some chance for the websocket
      context.system.scheduler.scheduleOnce(3 seconds, demoPlayer, PlayRecordedDemo(config.getString("demo.filename")))

    } else {
      // normal start
      val wsHandler = context.actorOf(WebSocketHandler.props, "websocket-handler")
      // todo: not the way to do it !
      FormulaOneTelemetry.webSocketHandler = Option(wsHandler)
      val changesHandler = context.actorOf(ModelChangesHandler.props)
      changesHandler ! RegisterHandler(wsHandler)

      val telemetryReceiver = context.actorOf(TelemetryReceiver.props(changesHandler))
      context.actorOf(UdpTelemetryPacketReceiver.props(telemetryReceiver, host, port))
    }
  }

  override def receive = {
    case _ =>
  }
}
