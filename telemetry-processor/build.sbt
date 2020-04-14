organization := "rocks.telemetry"
name := "telemetry-processor"

scalaVersion := "2.12.11"

val logbackV = "1.2.3"
val kafkaStreamsV = "2.4.1"

libraryDependencies ++= Seq(

 "org.apache.kafka" % "kafka-streams-scala_2.12" % kafkaStreamsV,

  // logging
  "ch.qos.logback" % "logback-classic" % logbackV
)

fork in run := true
