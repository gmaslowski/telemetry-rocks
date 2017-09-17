organization := "com.gmaslowski"
name := "f1game-telemetry"

scalaVersion := "2.12.3"

val akkaV = "2.5.4"

libraryDependencies ++= Seq(

  // akka
  "com.typesafe.akka" %% "akka-actor" % akkaV,
  "com.typesafe.akka" %% "akka-slf4j" % akkaV,

  // logging
  "ch.qos.logback" % "logback-classic" % "1.2.3"
)

