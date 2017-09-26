package com.gmaslowski.telemetry.live.model

import akka.actor.{Actor, ActorLogging, Props}

object LapInformation {
  def props = Props(classOf[LapInformation])
}

class LapInformation extends Actor with ActorLogging {
  override def receive = ???
}
