import com.typesafe.sbt.packager.docker.{Cmd, ExecCmd}

enablePlugins(JavaAppPackaging)
enablePlugins(DockerPlugin)

organization := "rocks.telemetry"
name := "telemetry-core"

scalaVersion := "2.12.4"

val akkaV = "2.5.7"
val akkaHttpV = "10.0.10"
val logbackV = "1.2.3"

libraryDependencies ++= Seq(
  // akka
  "com.typesafe.akka" %% "akka-actor" % akkaV,
  "com.typesafe.akka" %% "akka-slf4j" % akkaV,
  "com.typesafe.akka" %% "akka-stream" % akkaV,
  "com.typesafe.akka" %% "akka-http" % akkaHttpV,
  "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpV,

  "com.google.cloud" % "google-cloud-pubsub" % "0.28.0-beta",

  // logging
  "ch.qos.logback" % "logback-classic" % logbackV
)

dockerCommands := Seq(
  Cmd("FROM gmaslowski/jdk:8"),
  Cmd("MAINTAINER Greg Maslowski <gregmaslowski@gmail.com>"),
  Cmd("WORKDIR /opt/docker"),
  Cmd("ADD opt /opt"),
  Cmd("RUN apk update && apk upgrade && apk add bash"),
  Cmd("RUN chown -R daemon:daemon ."),
  Cmd("EXPOSE 8001"),
  Cmd("USER daemon"),
  Cmd("ENTRYPOINT", "bin/telemetry-core", "-Dconfig.resource=production.conf")
)