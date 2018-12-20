package rocks.telemetry.core.ws.frontend

import akka.actor.{Actor, ActorLogging, Props}
import rocks.telemetry.core.live.model.CarConfigurationInformation.CarConfiguration
import rocks.telemetry.core.live.model.LapInformation.LapData
import rocks.telemetry.core.live.model.LiveInformation.LiveData
import rocks.telemetry.core.live.model.TelemetryModel.TelemetryDataWithPlayer
import rocks.telemetry.core.ws.frontend.WebSocketHandler.JsonApiWithPlayer
import rocks.telemetry.core.ws.frontend.api.TelemetryWebSocketApi._

object ToJsonConverter {
  def props = Props(classOf[ToJsonConverter])
}

class ToJsonConverter extends Actor with ActorLogging {
  override def receive = {
    case TelemetryDataWithPlayer(player, telemetryData) =>
      telemetryData match {

        case liveData: LiveData =>
          sender() ! JsonApiWithPlayer(player,
            LiveDataJson(liveData.speed,
              liveData.gear,
              liveData.revs,
              liveData.throttle,
              liveData.break,
              liveData.x,
              liveData.y,
              liveData.lapTime))

        case lapData: LapData =>
          sender() ! JsonApiWithPlayer(player,
            LapDataJson(lapData.sector1,
              lapData.sector2,
              lapData.sector3,
              lapData.lap))

        case carConfiguration: CarConfiguration =>
          sender() ! JsonApiWithPlayer(player,
            CarConfigurationJson(
              TyreJson(
                carConfiguration.tyre.compound,
                carConfiguration.tyre.compoundName
              ),
              TeamJson(
                carConfiguration.team.team,
                carConfiguration.team.teamName
              ),
              EngineJson(
                carConfiguration.engine.idleRevs,
                carConfiguration.engine.maxRevs
              )
            ))
      }
  }
}
