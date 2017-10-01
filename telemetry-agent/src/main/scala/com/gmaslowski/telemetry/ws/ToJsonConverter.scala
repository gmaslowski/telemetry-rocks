package com.gmaslowski.telemetry.ws

import akka.actor.{Actor, ActorLogging, Props}
import com.gmaslowski.telemetry.live.model.CarConfigurationInformation.CarConfiguration
import com.gmaslowski.telemetry.live.model.LapInformation.LapData
import com.gmaslowski.telemetry.live.model.LiveInformation.LiveData
import com.gmaslowski.telemetry.live.model.TelemetryModel.TelemetryData
import com.gmaslowski.telemetry.ws.api.TelemetryWebSocketApi._

object ToJsonConverter {
  def props = Props(classOf[ToJsonConverter])
}

class ToJsonConverter extends Actor with ActorLogging {
  override def receive = {
    case telemetryData: TelemetryData =>
      telemetryData match {
        case liveData: LiveData =>
          sender() ! LiveDataJson(liveData.speed,
            liveData.gear,
            liveData.revs,
            liveData.throttle,
            liveData.break,
            liveData.x,
            liveData.y)
        case lapData: LapData =>
          sender() ! LapDataJson(lapData.sector1,
            lapData.sector2,
            lapData.lap)
        case carConfiguration: CarConfiguration =>
          sender() ! CarConfigurationJson(
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
          )
      }
  }
}
