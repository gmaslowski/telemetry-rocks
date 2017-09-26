package com.gmaslowski.telemetry.live.model

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import akka.util.ByteString
import com.gmaslowski.telemetry.ByteStringReader._
import com.gmaslowski.telemetry.F1Enums._
import com.gmaslowski.telemetry.live.model.CarConfigurationInformation.{CarConfiguration, Engine, Team, Tyre}
import com.gmaslowski.telemetry.ws.api.TelemetryWebSocketApi.JsonApi

object CarConfigurationInformation {
  def props = Props(classOf[CarConfigurationInformation])

  // api
  case class CarConfiguration(tyre: Tyre, team: Team, engine: Engine, dataType: String = "CarConfiguration") extends JsonApi
  case class Tyre(compound: Int, compoundName: String)
  case class Team(team: Int, teamName: String)
  case class Engine(idleRevs: Int, maxRevs: Int)
}

class CarConfigurationInformation extends Actor with ActorLogging {

  var carConfiguration: CarConfiguration = _

  override def receive = {
    case (packet: ByteString, receiver: ActorRef) =>

      val receivedCarConfiguration = CarConfiguration(
        Tyre(packet.m_tyre_compound, packet.m_tyre_compound.asTyre),
        Team(packet.m_team_info.toInt, packet.m_team_info.toInt.asTeam),
        Engine(packet.m_idle_rpm.toInt, packet.m_max_rpm.toInt)
      )

      if(receivedCarConfiguration != carConfiguration) {
        carConfiguration = receivedCarConfiguration
        receiver ! carConfiguration
      }
  }
}
