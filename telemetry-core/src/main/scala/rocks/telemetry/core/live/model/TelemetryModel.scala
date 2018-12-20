package rocks.telemetry.core.live.model

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import akka.util.ByteString
import rocks.telemetry.core.live.model.TelemetryReceiver.Player

object TelemetryModel {
  def props(changesHandler: ActorRef, player: Player) = Props(classOf[TelemetryModel], changesHandler, player)

  trait TelemetryData
  case class TelemetryDataWithPlayer(player: Player, telemetryData: TelemetryData)
}

class TelemetryModel(val changesHandler: ActorRef, player: Player) extends Actor with ActorLogging {

  val informationActors: Seq[ActorRef] = Seq(
    context.actorOf(LiveInformation.props(player)),
    context.actorOf(CarConfigurationInformation.props(player)),
    context.actorOf(LapInformation.props(player))
  )

  override def receive = {
    case packet: ByteString =>
      informationActors.foreach(actor => actor ! (packet, changesHandler))
  }
}
