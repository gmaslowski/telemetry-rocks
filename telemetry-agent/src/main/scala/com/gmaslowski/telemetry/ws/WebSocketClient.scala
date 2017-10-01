package com.gmaslowski.telemetry.ws

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import com.gmaslowski.telemetry.ws.WebSocketHandler.RegisterWsClient
import com.gmaslowski.telemetry.ws.api.TelemetryWebSocketApi.JsonApi

object WebSocketClient {
  def props(webSocketHandler: ActorRef, out: ActorRef) = Props(classOf[WebSocketClient], webSocketHandler, out)
}

class WebSocketClient(val webSocketHandler: ActorRef, val out: ActorRef) extends Actor with ActorLogging {

  webSocketHandler ! RegisterWsClient(self)

  override def receive = {
    case message: JsonApi => out ! message
  }
}
