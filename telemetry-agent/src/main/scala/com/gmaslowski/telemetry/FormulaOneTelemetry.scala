package com.gmaslowski.telemetry

import akka.actor._
import com.gmaslowski.telemetry.ModelChangesHandler.RegisterHandler
import com.gmaslowski.telemetry.demo.TelemetryDemoPlayer.PlayRecordedDemo
import com.gmaslowski.telemetry.demo.{TelemetryDemoPlayer, TelemetryDemoRecorder}
import com.gmaslowski.telemetry.udp.UdpTelemetryPacketReceiver
import com.gmaslowski.telemetry.ws.WebSocketHandler
import com.typesafe.config.ConfigFactory

import scala.concurrent.duration._

object FormulaOneTelemetry extends Akka.AkkaThings {
  implicit val ec = system.dispatcher

  val config = ConfigFactory.defaultApplication()
  val host = config.getString("listen.host")
  val port = config.getInt("listen.port")

  val (wsHandler, changesHandler) = resolve

  def resolve: (ActorRef, ActorRef) = {
    if (config.getBoolean("demo.record") && config.getString("demo.filename") != null) {
      // record demo mode
      val wsHandler = system.actorOf(WebSocketHandler.props, "websocket-handler")
      val changesHandler = system.actorOf(ModelChangesHandler.props)
      changesHandler ! RegisterHandler(wsHandler)

      val demoRecorder = system.actorOf(TelemetryDemoRecorder.props(config.getString("demo.filename")))
      val packetReceiver = system.actorOf(UdpTelemetryPacketReceiver.props(demoRecorder, host, port))

      (wsHandler, changesHandler)
    } else if (config.getBoolean("demo.play") && config.getString("demo.filename") != null) {

      // play demo mode
      val wsHandler = system.actorOf(WebSocketHandler.props, "websocket-handler")
      val changesHandler = system.actorOf(ModelChangesHandler.props)
      changesHandler ! RegisterHandler(wsHandler)

      val packetTransformer = system.actorOf(TelemetryReceiver.props(changesHandler))
      val demoPlayer = system.actorOf(TelemetryDemoPlayer.props(packetTransformer))

      // give some chance for the websocket
      system.scheduler.scheduleOnce(3 seconds, demoPlayer, PlayRecordedDemo(config.getString("demo.filename")))

      (wsHandler, changesHandler)
    } else {
      // normal start
      val wsHandler = system.actorOf(WebSocketHandler.props, "websocket-handler")
      val changesHandler = system.actorOf(ModelChangesHandler.props)
      changesHandler ! RegisterHandler(wsHandler)

      val telemetryReceiver = system.actorOf(TelemetryReceiver.props(changesHandler))
      system.actorOf(UdpTelemetryPacketReceiver.props(telemetryReceiver, host, port))
      (wsHandler, changesHandler)
    }
  }
}
