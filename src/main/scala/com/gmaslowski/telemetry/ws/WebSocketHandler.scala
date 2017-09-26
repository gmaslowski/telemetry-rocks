package com.gmaslowski.telemetry.ws

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import com.gmaslowski.telemetry.ws.WebSocketHandler.RegisterWsClient
import com.gmaslowski.telemetry.ws.api.TelemetryWebSocketApi.JsonApi

object WebSocketHandler {
  def props = Props(classOf[WebSocketHandler])

  case class RegisterWsClient(client: ActorRef)
}

class WebSocketHandler extends Actor with ActorLogging {

  var clients: List[ActorRef] = List.empty

  override def receive = {
    case RegisterWsClient(client) =>
      clients = client :: clients

      // fixme: unify sent messages
    case message: JsonApi => clients.foreach(_ ! message)
  }
}
