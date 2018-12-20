package rocks.telemetry.core.live.model

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import akka.util.ByteString
import rocks.telemetry.core.live.model.TelemetryReceiver.Player

object TelemetryReceiver {
  def props(changesHandler: ActorRef, email: String) = Props(classOf[TelemetryReceiver], changesHandler, email)

  case class Player(email: String)
}

class TelemetryReceiver(val changesHandler: ActorRef, val email: String) extends Actor with ActorLogging {

  val player = Player(email)
  val telemetryModel = context.actorOf(TelemetryModel.props(changesHandler, player))

  override def receive = {
    case packet: ByteString =>
      telemetryModel ! packet
  }
}