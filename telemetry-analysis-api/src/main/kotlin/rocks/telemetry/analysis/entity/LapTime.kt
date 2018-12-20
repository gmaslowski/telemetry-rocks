package rocks.telemetry.analysis.entity

import org.springframework.data.annotation.Id
import org.springframework.data.annotation.TypeAlias
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "lapTimes")
@TypeAlias("lapTimes")
data class LapTime(@Id val id: String? = null,
                   val s1: Int? = null,
                   val s2: Int? = null,
                   val s3: Int? = null,
                   val lapTime: Int? = null,
                   val user: String? = null,
                   val track: String? = null)

