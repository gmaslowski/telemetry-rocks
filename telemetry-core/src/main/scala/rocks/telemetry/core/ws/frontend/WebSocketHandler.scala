package rocks.telemetry.core.ws.frontend

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import akka.pattern.{ask, pipe}
import akka.util.Timeout
import rocks.telemetry.core.live.model.TelemetryModel.TelemetryDataWithPlayer
import rocks.telemetry.core.live.model.TelemetryReceiver.Player
import rocks.telemetry.core.ws.frontend.WebSocketHandler.{JsonApiWithPlayer, RegisterWsClient}
import rocks.telemetry.core.ws.frontend.api.TelemetryWebSocketApi.JsonApi

import scala.concurrent.ExecutionContext
import scala.concurrent.duration._

object WebSocketHandler {
  def props = Props(classOf[WebSocketHandler])

  case class RegisterWsClient(player: Player, client: ActorRef)
  case class JsonApiWithPlayer(player: Player, jsonApi: JsonApi)
}

class WebSocketHandler extends Actor with ActorLogging {

  implicit val ec: ExecutionContext = context.dispatcher
  implicit val timeout: Timeout = 10 milliseconds

  var clients: Map[Player, ActorRef] = Map.empty
  val toJsonConverter: ActorRef = context.actorOf(ToJsonConverter.props)

  override def receive = {
    case RegisterWsClient(player, client) =>
      log.info(s"Registering new WS client $client for $player")
      clients = clients + (player -> client)

    case telemetryDataWithPlayer: TelemetryDataWithPlayer =>
      toJsonConverter ? telemetryDataWithPlayer pipeTo self

    case jsonApiWithPlayer: JsonApiWithPlayer =>
      clients.get(jsonApiWithPlayer.player).foreach(_ ! jsonApiWithPlayer.jsonApi )
  }
}