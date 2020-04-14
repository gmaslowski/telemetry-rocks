package rocks.telemetry.bootstrap

import java.nio.file.{Files, Paths}

object FillTelemetry extends App {

  import java.util.Properties

  import org.apache.kafka.clients.producer._
  import rocks.telemetry.f12017.ArrayByteReader._

  val props = new Properties()
  props.put("bootstrap.servers", "localhost:9092")
  props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
  props.put("value.serializer", "org.apache.kafka.common.serialization.ByteArraySerializer")

  val producer = new KafkaProducer[String, Array[Byte]](props)

  val PacketSize = 1237

  val path = getClass.getResource("../../../demo.f1t").getPath
  val readPackets = Files.readAllBytes(Paths.get(path))
  val packetsIterator = readPackets.grouped(PacketSize)

  while (packetsIterator.hasNext) {
    val bytes = packetsIterator.next()

    println((bytes.m_last_lap_time * 1000).toInt)
//    println(bytes.m_sector)
    val record: ProducerRecord[String, Array[Byte]] = new ProducerRecord("rocks.telemetry.raw-telemetry", bytes)
    producer.send(record)
  }

  producer.close()

}
