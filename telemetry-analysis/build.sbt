import com.typesafe.sbt.packager.docker.{Cmd, ExecCmd}

enablePlugins(JavaAppPackaging)
enablePlugins(DockerPlugin)

organization := "rocks.telemetry"
name := "telemetry-analysis"

scalaVersion := "2.11.11"

fork := true

val sparkV = "2.2.0"
val logbackV = "1.2.3"

libraryDependencies ++= Seq(
  // spark
  "org.apache.spark" %% "spark-streaming" % sparkV,
  "org.apache.bahir" %% "spark-streaming-pubsub" % sparkV,
  "org.mongodb.spark" %% "mongo-spark-connector" % sparkV,
  "org.apache.spark" %% "spark-core" % sparkV,
  "org.apache.spark" %% "spark-sql" % sparkV,

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
  Cmd("USER daemon"),
  Cmd("ENTRYPOINT", "bin/telemetry-analysis")
)
