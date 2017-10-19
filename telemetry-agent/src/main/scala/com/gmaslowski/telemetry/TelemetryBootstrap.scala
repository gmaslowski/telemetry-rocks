package com.gmaslowski.telemetry

import akka.http.scaladsl.Http
import com.gmaslowski.telemetry.controllers.WebSocketController._

object TelemetryBootstrap extends App with Akka.AkkaThings {

  Http().bindAndHandle(route, "0.0.0.0", 8081)
}
