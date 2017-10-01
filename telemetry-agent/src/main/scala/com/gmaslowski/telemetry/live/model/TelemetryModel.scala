package com.gmaslowski.telemetry.live.model

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import akka.util.ByteString

object TelemetryModel {
  def props(changesHandler: ActorRef) = Props(classOf[TelemetryModel], changesHandler)

  trait TelemetryData
}

class TelemetryModel(val changesHandler: ActorRef) extends Actor with ActorLogging {

  val informationActors: Seq[ActorRef] = Seq(
    context.actorOf(LiveInformation.props),
    context.actorOf(CarConfigurationInformation.props),
    context.actorOf(LapInformation.props)
  )

  override def receive = {
    case packet: ByteString =>
      informationActors.foreach(actor => actor ! (packet, changesHandler))
  }
}
