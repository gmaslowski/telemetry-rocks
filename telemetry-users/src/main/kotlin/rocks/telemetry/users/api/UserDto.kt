package rocks.telemetry.users.api

data class UserWithPasswordDto(val id: String,
                               val email: String,
                               val firstName: String,
                               val lastName: String,
                               val passwordHash: String,
                               val passwordSalt: String)

data class UserDto(val id: String,
                   val uuid: String,
                   val firstName: String,
                   val lastName: String)

data class NewUserDto(val email: String,
                      val password: String,
                      val firstName: String,
                      val lastName: String)

data class ApiKeyVerificationDto(val apiKey: String)

data class ApiVerifiedDto(val boolean: Boolean)