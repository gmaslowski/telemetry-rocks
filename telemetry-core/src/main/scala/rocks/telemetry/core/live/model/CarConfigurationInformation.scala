package rocks.telemetry.core.live.model

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import akka.util.ByteString
import rocks.telemetry.core.live.model.ByteStringReader._
import rocks.telemetry.core.live.model.CarConfigurationInformation.{CarConfiguration, Engine, Team, Tyre}
import rocks.telemetry.core.live.model.F1Enums._
import rocks.telemetry.core.live.model.TelemetryModel.{TelemetryData, TelemetryDataWithPlayer}
import rocks.telemetry.core.live.model.TelemetryReceiver.Player

object CarConfigurationInformation {
  def props(player: Player) = Props(classOf[CarConfigurationInformation], player)

  // api
  case class CarConfiguration(tyre: Tyre,
                              team: Team,
                              engine: Engine,
                              dataType: String = "CarConfiguration") extends TelemetryData

  case class Tyre(compound: Int, compoundName: String)

  case class Team(team: Int, teamName: String)

  case class Engine(idleRevs: Int, maxRevs: Int)

}

class CarConfigurationInformation(val player: Player) extends Actor with ActorLogging {

  var carConfiguration: CarConfiguration = _

  override def receive = {
    case (packet: ByteString, changesHandler: ActorRef) =>

      val receivedCarConfiguration = CarConfiguration(
        Tyre(packet.m_tyre_compound, packet.m_tyre_compound.asTyre),
        Team(packet.m_team_info.toInt, packet.m_team_info.toInt.asTeam),
        Engine(packet.m_idle_rpm.toInt, packet.m_max_rpm.toInt)
      )

      if (receivedCarConfiguration != carConfiguration) {
        carConfiguration = receivedCarConfiguration
        changesHandler ! TelemetryDataWithPlayer(player, carConfiguration)
      }
  }
}
