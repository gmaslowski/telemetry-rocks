package com.gmaslowski.telemetry.ws

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import com.gmaslowski.telemetry.ws.WebSocketHandler.Register
import com.gmaslowski.telemetry.ws.api.TelemetryWebSocketApi.CarData

object WebSocketClient {
  def props(webSocketHandler: ActorRef, out: ActorRef) = Props(classOf[WebSocketClient], webSocketHandler, out)
}

class WebSocketClient(val webSocketHandler: ActorRef, val out: ActorRef) extends Actor with ActorLogging {

  webSocketHandler ! Register(self)

  override def receive = {
    case message: CarData => out ! message
  }
}
