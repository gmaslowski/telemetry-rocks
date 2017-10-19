package com.gmaslowski.telemetry

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer

object Akka {

  trait AkkaThings {
    implicit val system = ActorSystem("rocks-telemetry")
    implicit val materializer = ActorMaterializer()
  }

}
