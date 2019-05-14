package rocks.telemetry.agent.akka

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer

object AkkaComponents {

  trait Requires

  trait Provides {

    val system: ActorSystem
    val materializer: ActorMaterializer
  }

  trait Default
    extends Provides
      with Requires {

    implicit val system: ActorSystem = ActorSystem("telemetry-agent")
    implicit val materializer: ActorMaterializer = ActorMaterializer()
  }
}
