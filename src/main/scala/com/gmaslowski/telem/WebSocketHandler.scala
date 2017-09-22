package com.gmaslowski.telem

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import com.gmaslowski.telem.WebSocketHandler.Register
import com.gmaslowski.telem.api.TelemetryWebSocketApi
import com.gmaslowski.telem.api.TelemetryWebSocketApi.CarData

object WebSocketHandler {
  def props = Props(classOf[WebSocketHandler])

  case class Register(client: ActorRef)
}

class WebSocketHandler extends Actor with ActorLogging {

  var clients: List[ActorRef] = List.empty

  override def receive = {
    case Register(client) =>
      clients = client :: clients
      client ! TelemetryWebSocketApi.EmptyCarData

    case message: CarData => clients.foreach(_ ! message)
  }
}
