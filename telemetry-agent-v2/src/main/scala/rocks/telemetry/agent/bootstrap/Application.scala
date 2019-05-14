package rocks.telemetry.agent.bootstrap

import rocks.telemetry.agent.akka.AkkaComponents
import rocks.telemetry.agent.broadcast.TelemetryBroadcastingComponents
import rocks.telemetry.agent.model.TelemetryModelComponents
import rocks.telemetry.agent.receiver.TelemetryReceiverComponents

object Application
  extends App
    with AkkaComponents.Default
    with TelemetryModelComponents.Default
    with TelemetryReceiverComponents.Demo
    with TelemetryBroadcastingComponents.Telnet
    with TelemetryBroadcastingComponents.WebSocket
    with TelemetryBroadcastingComponents.Logger
