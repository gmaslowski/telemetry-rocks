package com.gmaslowski.telem

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import com.gmaslowski.telem.WebSocketHandler.Register
import com.gmaslowski.telem.api.TelemetryWebSocketApi.CarData

object WebSocketClient {
  def props(webSocketHandler: ActorRef, out: ActorRef) = Props(classOf[WebSocketClient], webSocketHandler, out)
}

class WebSocketClient(val webSocketHandler: ActorRef, val out: ActorRef) extends Actor with ActorLogging {

  webSocketHandler ! Register(self)

  override def receive = {
    case message: CarData => out ! message
  }
}
