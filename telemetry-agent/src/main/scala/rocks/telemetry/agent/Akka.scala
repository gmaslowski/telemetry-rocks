package rocks.telemetry.agent

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer

object Akka {

  trait AkkaThings {
    implicit val system = ActorSystem("telemetry-agent")
    implicit val materializer = ActorMaterializer()
  }

}
