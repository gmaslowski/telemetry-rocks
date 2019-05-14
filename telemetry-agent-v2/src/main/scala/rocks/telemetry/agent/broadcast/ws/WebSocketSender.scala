package rocks.telemetry.agent.broadcast.ws

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import akka.util.Timeout
import WebSocketSender.RegisterWsConnection

import scala.concurrent.ExecutionContext
import scala.concurrent.duration._

object WebSocketSender {
  def props = Props(classOf[WebSocketSender])

  case class RegisterWsConnection(webSocketConnection: ActorRef)

}

class WebSocketSender extends Actor with ActorLogging {

  implicit val ec: ExecutionContext = context.dispatcher
  implicit val timeout: Timeout = 10 milliseconds

  var connections: List[ActorRef] = List.empty

  override def receive = {
    case RegisterWsConnection(webSockerConnection) =>
      connections = webSockerConnection :: connections

    case message: String =>
      connections.foreach(_ ! message)
  }
}