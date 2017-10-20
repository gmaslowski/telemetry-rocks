package com.gmaslowski.telemetry.ws

import akka.actor.{Actor, ActorRef, Props}
import com.gmaslowski.telemetry.ws.SingleWebSocketConnection.{Connected, OutgoingMessage}
import com.gmaslowski.telemetry.ws.WebSocketHandler.RegisterWsClient
import com.gmaslowski.telemetry.ws.api.TelemetryWebSocketApi.JsonApi

object SingleWebSocketConnection {

  def props(wsHandler: ActorRef) = Props(classOf[SingleWebSocketConnection], wsHandler)

  case class Connected(outgoing: ActorRef)
  case class IncomingMessage(json: JsonApi)
  case class OutgoingMessage(json: JsonApi)
}

class SingleWebSocketConnection(wsHandler: ActorRef) extends Actor {

  def receive = {
    case Connected(outgoing) =>
      wsHandler ! RegisterWsClient(self)
      context.become(connected(outgoing))
  }

  def connected(outgoing: ActorRef): Receive = {
    case json: JsonApi =>
      outgoing ! OutgoingMessage(json)
  }
}
