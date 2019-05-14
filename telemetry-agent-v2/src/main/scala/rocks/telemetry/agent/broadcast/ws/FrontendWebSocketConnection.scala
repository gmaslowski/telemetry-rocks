package rocks.telemetry.agent.broadcast.ws

import akka.actor.{Actor, ActorRef, Props}
import FrontendWebSocketConnection.{Connected, OutgoingMessage}
import WebSocketSender.RegisterWsConnection

object FrontendWebSocketConnection {

  def props(wsHandler: ActorRef) = Props(classOf[FrontendWebSocketConnection], wsHandler)

  case class Connected(outgoing: ActorRef)
  case class IncomingMessage()
  case class OutgoingMessage(msg: String)

}

class FrontendWebSocketConnection(wsHandler: ActorRef) extends Actor {

  def receive = {
    case Connected(outgoing) =>
      wsHandler ! RegisterWsConnection(self)
      context.become(connected(outgoing))
  }

  def connected(outgoing: ActorRef): Receive = {
    case string: String =>
      outgoing ! OutgoingMessage(string)
  }
}
