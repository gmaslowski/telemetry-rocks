package rocks.telemetry.bootstrap

import java.time.Duration
import java.util.Properties

import org.apache.kafka.streams.scala.StreamsBuilder
import org.apache.kafka.streams.{KafkaStreams, StreamsConfig}

object Application
  extends App {

  import org.apache.kafka.streams.scala.ImplicitConversions._
  import org.apache.kafka.streams.scala.Serdes._
  import rocks.telemetry.f12017.ArrayByteReader._

  val config: Properties = {
    val p = new Properties()
    p.put(StreamsConfig.APPLICATION_ID_CONFIG, "telemetry-processor")
    p.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
    p
  }

  val builder = new StreamsBuilder

  builder.stream[Array[Byte], Array[Byte]]("rocks.telemetry.raw-telemetry")
    .mapValues(rawTelemetryData => {
      println(rawTelemetryData.m_speed)
      rawTelemetryData.m_speed
    })
    .to("rocks.telemetry.lap-times")

  val streams: KafkaStreams = new KafkaStreams(builder.build(), config)
  streams.start()

  sys.ShutdownHookThread {
    streams.close(Duration.ofSeconds(10))
  }

}
