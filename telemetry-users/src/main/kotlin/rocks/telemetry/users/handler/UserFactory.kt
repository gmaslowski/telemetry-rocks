package rocks.telemetry.users.handler

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import rocks.telemetry.users.api.NewUserDto
import rocks.telemetry.users.entity.User
import rocks.telemetry.users.generators.ApiToken
import rocks.telemetry.users.generators.Uuid
import rocks.telemetry.users.hashers.PasswordHasher

@Component
class UserFactory @Autowired constructor(val uuid: Uuid,
                                         val apiToken: ApiToken,
                                         val passwordHasher: PasswordHasher) {

    fun createUser(dto: NewUserDto): User {

        val password = passwordHasher.hash(dto.password)

        return User(email = dto.email,
            firstName = dto.firstName,
            lastName = dto.lastName,
            apiKey = apiToken.generate(),
            uuid = uuid.generate(),
            passwordHash = password.hash,
            passwordSalt = password.salt)
    }
}