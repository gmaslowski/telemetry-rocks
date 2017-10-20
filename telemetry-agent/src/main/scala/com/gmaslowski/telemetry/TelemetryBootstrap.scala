package com.gmaslowski.telemetry

import akka.actor._
import akka.http.scaladsl.Http
import com.gmaslowski.telemetry.FormulaOneTelemetry.config
import com.gmaslowski.telemetry.demo.TelemetryDemoPlayer.PlayRecordedDemo
import com.gmaslowski.telemetry.demo.{TelemetryDemoPlayer, TelemetryDemoRecorder}
import com.gmaslowski.telemetry.live.model.ModelChangesHandler.RegisterHandler
import com.gmaslowski.telemetry.live.model.{ModelChangesHandler, TelemetryReceiver}
import com.gmaslowski.telemetry.udp.UdpTelemetryPacketReceiver
import com.gmaslowski.telemetry.ws.WebSocketController._
import com.gmaslowski.telemetry.ws.WebSocketHandler
import com.typesafe.config.ConfigFactory

object TelemetryBootstrap extends App with Akka.AkkaThings {

  Http().bindAndHandle(route, config.getString("http.listen.host"), config.getInt("http.listen.port"))
}

object FormulaOneTelemetry extends Akka.AkkaThings {

  import scala.concurrent.duration._

  implicit val ec = system.dispatcher

  val config = ConfigFactory.defaultApplication()

  val (wsHandler, changesHandler) = resolve

  def resolve: (ActorRef, ActorRef) = {
    if (config.getBoolean("demo.record") && config.getString("demo.filename") != null) {
      // record demo mode
      val wsHandler = system.actorOf(WebSocketHandler.props, "websocket-handler")
      val changesHandler = system.actorOf(ModelChangesHandler.props)
      changesHandler ! RegisterHandler(wsHandler)

      val demoRecorder = system.actorOf(TelemetryDemoRecorder.props(config.getString("demo.filename")))
      val packetReceiver = system.actorOf(UdpTelemetryPacketReceiver.props(demoRecorder, config))

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
      system.actorOf(UdpTelemetryPacketReceiver.props(telemetryReceiver, config))
      (wsHandler, changesHandler)
    }
  }
}
