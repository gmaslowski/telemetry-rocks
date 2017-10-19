organization := "com.gmaslowski"
name := "f1game-telemetry"

scalaVersion := "2.12.3"

val akkaV = "2.5.4"
val akkaHttpV = "10.0.10"
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