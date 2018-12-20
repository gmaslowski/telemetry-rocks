package rocks.telemetry.analysis.api

data class LapTimeDto(val s1: Int,
                      val s2: Int,
                      val s3: Int,
                      val lap: Int,
                      val user: String,
                      val track: String)