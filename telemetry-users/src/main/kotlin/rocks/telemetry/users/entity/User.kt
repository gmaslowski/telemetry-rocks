package rocks.telemetry.users.entity

import org.springframework.data.annotation.Id
import org.springframework.data.annotation.TypeAlias
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "users")
@TypeAlias("users")
data class User(@Id val id: String? = null,
                val uuid: String? = null,
                val firstName: String? = null,
                val lastName: String? = null,
                val email: String? = null,
                val apiKey: String? = null,
                val passwordHash: String? = null,
                val passwordSalt: String? = null)

