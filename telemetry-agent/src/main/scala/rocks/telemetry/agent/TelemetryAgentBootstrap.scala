package rocks.telemetry.agent

import com.typesafe.config.ConfigFactory
import rocks.telemetry.agent.demo.TelemetryDemoPlayer.PlayRecordedDemo
import rocks.telemetry.agent.demo.{TelemetryDemoPlayer, TelemetryDemoRecorder}
import rocks.telemetry.agent.udp.UdpTelemetryPacketReceiver

object TelemetryAgentBootstrap extends App with Akka.AkkaThings {

  import scala.concurrent.duration._

  implicit val ec = system.dispatcher

  val config = ConfigFactory.load

  if (config.getBoolean("demo.record") && config.getString("demo.filename") != null) {
    // record demo mode
    val demoRecorder = system.actorOf(TelemetryDemoRecorder.props(config.getString("demo.filename")))
    val packetReceiver = system.actorOf(UdpTelemetryPacketReceiver.props(demoRecorder, config))

  } else if (config.getBoolean("demo.play") && config.getString("demo.filename") != null) {
    // play demo mode

    val forwarder = system.actorOf(TelemetryForwarder.props(config.getString("ws.listen"), config.getString("user.email"), config.getString("user.apiKey")), "forwarder")
    val demoPlayer = system.actorOf(TelemetryDemoPlayer.props(forwarder))

    // give some chance for the websocket
    system.scheduler.scheduleOnce(3 seconds, demoPlayer, PlayRecordedDemo(config.getString("demo.filename")))

  } else {
    // normal start
    val forwarder = system.actorOf(TelemetryForwarder.props(config.getString("ws.listen"), config.getString("user.email"), config.getString("user.apiKey")), "forwarder")
    system.actorOf(UdpTelemetryPacketReceiver.props(forwarder, config))
  }
}
