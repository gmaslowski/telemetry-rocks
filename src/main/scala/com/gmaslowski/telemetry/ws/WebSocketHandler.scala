package com.gmaslowski.telemetry.ws

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import com.gmaslowski.telemetry.ws.WebSocketHandler.Register
import com.gmaslowski.telemetry.ws.api.TelemetryWebSocketApi
import com.gmaslowski.telemetry.ws.api.TelemetryWebSocketApi.CarData

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
