package com.gmaslowski.telemetry.ws.api

import com.gmaslowski.telemetry.ws.api.TelemetryWebSocketApi.{CarData, Revs}
import play.api.libs.json.Json
import play.api.mvc.WebSocket.MessageFlowTransformer

object TelemetryWebSocketApi {

  case class CarData(speed: Int,
                     gear: Int,
                     tyreCompound: String,
                     team: String,
                     sector_1: Float,
                     sector_2: Float,
                     lap: Float,
                     revs: Revs)
  case class Revs(curRevs: Int, minRevs: Int, maxRevs: Int)
  case class CarInitialData(car: String)

  case class EngineInitialData()
  case class GearboxInitialData()

  val NoRevs = Revs(0, 0, 0)
  val EmptyCarData = CarData(0, 0, "", "", 0.0f, 0.0f, 0.0f, NoRevs)
}

trait TelemetryWebSocketApiTransformers {
  implicit val revsFormat = Json.format[Revs]
  implicit val carDataFormat = Json.format[CarData]

  implicit val messageFlowTransformer = MessageFlowTransformer.jsonMessageFlowTransformer[String, CarData]
}
