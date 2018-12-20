name := "telemetry-users-loadtests"
organization in ThisBuild := "rocks.telemetry.users"
version in ThisBuild := "1.0-SNAPSHOT"

scalaVersion in ThisBuild := "2.12.3"

val gatling = "io.gatling.highcharts" % "gatling-charts-highcharts" % "2.3.0"

libraryDependencies ++= Seq(
  gatling
)

