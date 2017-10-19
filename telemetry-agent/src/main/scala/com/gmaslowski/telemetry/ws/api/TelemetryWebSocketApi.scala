package com.gmaslowski.telemetry.ws.api

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import com.gmaslowski.telemetry.ws.api.TelemetryWebSocketApi._
import spray.json.DefaultJsonProtocol

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

trait JsonSupport extends SprayJsonSupport {

  import DefaultJsonProtocol._
  import spray.json._

  implicit val teamJsonFormat = jsonFormat2(TeamJson)
  implicit val tyreJsonFormat = jsonFormat2(TyreJson)
  implicit val engineJsonFormat = jsonFormat2(EngineJson)
  implicit val carConfigurationJsonFormat = jsonFormat4(CarConfigurationJson)

  implicit val lapDataJsonFormat = jsonFormat4(LapDataJson)
  implicit val liveDataJsonFormat = jsonFormat8(LiveDataJson)


  def write(json: JsonApi) = json match {
    case carConfiguration: CarConfigurationJson => carConfiguration.toJson
    case liveData: LiveDataJson => liveData.toJson
    case lapData: LapDataJson => lapData.toJson
  }
}