package rocks.telemetry.analysis.handler

import rocks.telemetry.analysis.api.LapTimeDto
import rocks.telemetry.analysis.entity.LapTime

fun LapTime.asDto() = LapTimeDto(s1!!, s2!!, s3!!, lapTime!!, user!!, track!!)
