package rocks.telemetry.agent.broadcast.audio

import akka.actor.{Actor, ActorLogging, Props}
import rocks.telemetry.agent.broadcast.TelemetryBroadcastListener
import rocks.telemetry.agent.model.api.Engine
import rocks.telemetry.agent.sound.SoundPlayer

object TelemetryAudioBroadcaster {
  def props = Props(classOf[TelemetryAudioBroadcaster])
}

class TelemetryAudioBroadcaster
  extends Actor
    with ActorLogging
    with TelemetryBroadcastListener {

  val soundPlayer: SoundPlayer = new SoundPlayer()
  soundPlayer.play()

  override def receive = {
    case engine: Engine => soundPlayer.setup(engine.currentRevs)

  }
}


