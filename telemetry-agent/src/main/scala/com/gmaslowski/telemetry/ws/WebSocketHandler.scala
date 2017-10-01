package com.gmaslowski.telemetry.ws

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import akka.pattern.{ask, pipe}
import akka.util.Timeout
import com.gmaslowski.telemetry.live.model.TelemetryModel.TelemetryData
import com.gmaslowski.telemetry.ws.WebSocketHandler.RegisterWsClient
import com.gmaslowski.telemetry.ws.api.TelemetryWebSocketApi.JsonApi

import scala.concurrent.ExecutionContext
import scala.concurrent.duration._

object WebSocketHandler {
  def props = Props(classOf[WebSocketHandler])

  case class RegisterWsClient(client: ActorRef)
}

class WebSocketHandler extends Actor with ActorLogging {

  implicit val ec: ExecutionContext = context.dispatcher
  implicit val timeout: Timeout = 10 milliseconds

  var clients: List[ActorRef] = List.empty
  val toJsonConverter: ActorRef = context.actorOf(ToJsonConverter.props)

  override def receive = {
    case RegisterWsClient(client) =>
      clients = client :: clients

    case message: TelemetryData =>
      toJsonConverter ? message pipeTo self

    case message: JsonApi =>
      clients.foreach(_ ! message)
  }
}
