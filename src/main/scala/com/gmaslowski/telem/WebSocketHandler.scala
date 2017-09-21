package com.gmaslowski.telem

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import com.gmaslowski.telem.WebSocketHandler.Register

object WebSocketHandler {
  def props = Props(classOf[WebSocketHandler])

  case class Register(client: ActorRef)
}

class WebSocketHandler extends Actor with ActorLogging {

  var clients: List[ActorRef] = List.empty

  override def receive = {
    case Register(client) =>
      clients = client :: clients
      client ! "WebSocket-Initialized"

    case message: String => clients.foreach(_ ! message)
  }
}
