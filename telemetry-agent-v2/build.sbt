organization := "com.ista"
name := "telemetry-agent-v2"

scalaVersion := "2.12.8"

val akkaV = "2.5.22"
val akkaHttpV = "10.1.8"
val logbackV = "1.2.3"

libraryDependencies ++= Seq(
  // akka
  "com.typesafe.akka" %% "akka-actor" % akkaV,
  "com.typesafe.akka" %% "akka-slf4j" % akkaV,
  "com.typesafe.akka" %% "akka-stream" % akkaV,
  "com.typesafe.akka" %% "akka-http" % akkaHttpV,
  "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpV,

  // logging
  "ch.qos.logback" % "logback-classic" % logbackV
)

fork in run := true
