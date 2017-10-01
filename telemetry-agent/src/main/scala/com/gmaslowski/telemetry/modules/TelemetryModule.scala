package com.gmaslowski.telemetry.modules

import com.gmaslowski.telemetry.TelemetryBootstrap
import com.google.inject.AbstractModule
import play.api.libs.concurrent.AkkaGuiceSupport

class TelemetryModule extends AbstractModule with AkkaGuiceSupport {
  override def configure() = {
    bind(classOf[TelemetryBootstrap]).asEagerSingleton()
  }
}
