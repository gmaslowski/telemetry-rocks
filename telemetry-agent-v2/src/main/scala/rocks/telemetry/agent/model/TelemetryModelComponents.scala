package rocks.telemetry.agent.model

import akka.actor.{ActorRef, ActorSystem}

object TelemetryModelComponents {

  trait Requires {
    val system: ActorSystem
  }

  trait Provides {
    val telemetryTranslator: ActorRef
  }

  trait Default extends Requires with Provides {
    val telemetryTranslator: ActorRef = system.actorOf(TelemetryTranslator.props, "telemetry-translator")
  }
}
