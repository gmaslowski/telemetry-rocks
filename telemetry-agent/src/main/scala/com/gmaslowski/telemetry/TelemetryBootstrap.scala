package com.gmaslowski.telemetry

import javax.inject.{Inject, Singleton}

import akka.actor.ActorSystem

@Singleton
class TelemetryBootstrap @Inject()(system: ActorSystem) {
  system.actorOf(FormulaOneTelemetry.props,"f1")
}
