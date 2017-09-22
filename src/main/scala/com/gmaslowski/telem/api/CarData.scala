package com.gmaslowski.telem.api

import com.gmaslowski.telem.api.TelemetryWebSocketApi.{CarData, Revs}
import play.api.libs.json.Json
import play.api.mvc.WebSocket.MessageFlowTransformer

object TelemetryWebSocketApi {

  case class CarData(speed: Int, gear: Int, revs: Revs)

  case class Revs(curRevs: Int, minRevs: Int, maxRevs: Int)

  val NoRevs = Revs(0, 0, 0)
  val EmptyCarData = CarData(0, 0, NoRevs)
}

trait TelemetryWebSocketApiTransformers {
  implicit val revsFormat = Json.format[Revs]
  implicit val carDataFormat = Json.format[CarData]

  implicit val messageFlowTransformer = MessageFlowTransformer.jsonMessageFlowTransformer[String, CarData]
}
