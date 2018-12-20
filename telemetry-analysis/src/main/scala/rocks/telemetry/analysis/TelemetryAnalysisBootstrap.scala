package rocks.telemetry.analysis

import rocks.telemetry.analysis.spark.SparkProcessing

object TelemetryAnalysisBootstrap extends App {

  SparkProcessing.startSpark
}
