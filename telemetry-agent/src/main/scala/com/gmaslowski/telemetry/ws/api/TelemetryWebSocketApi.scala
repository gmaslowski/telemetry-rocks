package com.gmaslowski.telemetry.ws.api

import com.gmaslowski.telemetry.ws.api.TelemetryWebSocketApi._
import play.api.libs.json.{Json, Writes}
import play.api.mvc.WebSocket.MessageFlowTransformer

object TelemetryWebSocketApi {

  trait JsonApi

  case class LiveDataJson(speed: Int,
                          gear: Int,
                          revs: Int,
                          throttle: Float,
                          break: Float,
                          x: Float,
                          y: Float,
                          dataType: String = "LiveData") extends JsonApi

  val EmptyLiveData = LiveDataJson(0, 0, 0, 0.0f, 0.0f, 0.0f, 0.0f)

  case class LapDataJson(sector1: Float,
                         sector2: Float,
                         lap: Float,
                         dataType:
                         String = "LapData") extends JsonApi

  val emptyLapData = LapDataJson(0.0f, 0.0f, 0.0f)

  case class CarConfigurationJson(tyre: TyreJson,
                                  team: TeamJson,
                                  engine: EngineJson,
                                  dataType: String = "CarConfiguration") extends JsonApi

  case class TyreJson(compound: Int, compoundName: String)

  case class TeamJson(team: Int, teamName: String)

  case class EngineJson(idleRevs: Int, maxRevs: Int)

}

trait TelemetryWebSocketApiTransformers {

  implicit val jsonApiWrites: Writes[JsonApi] = {
    case s: LiveDataJson => liveDataWrites.writes(s)
    case s: CarConfigurationJson => carConfigurationWrites.writes(s)
    case s: LapDataJson => lapDataWrites.writes(s)
  }

  implicit val liveDataWrites = Json.writes[LiveDataJson]
  implicit val lapDataWrites = Json.writes[LapDataJson]
  implicit val tyreWrites = Json.writes[TyreJson]
  implicit val engineWrites = Json.writes[EngineJson]
  implicit val teamWrites = Json.writes[TeamJson]
  implicit val carConfigurationWrites = Json.writes[CarConfigurationJson]

  implicit val jsonApiTransformer = MessageFlowTransformer.jsonMessageFlowTransformer[String, JsonApi]
}
