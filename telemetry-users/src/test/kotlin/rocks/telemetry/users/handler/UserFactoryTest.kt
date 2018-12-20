package rocks.telemetry.users.handler

import io.kotlintest.matchers.shouldBe
import io.kotlintest.matchers.shouldNotBe
import io.kotlintest.specs.FreeSpec
import rocks.telemetry.users.api.NewUserDto
import rocks.telemetry.users.generators.ApiToken
import rocks.telemetry.users.generators.Uuid
import rocks.telemetry.users.hashers.PasswordHasher

class UserFactoryTest : FreeSpec() {

    companion object {
        val EMAIL = "email"
        val FIRSTNAME = "firstName"
        val LASTNAME = "lastName"
        val PASSWORD = "myPass"

        val newUserDto: NewUserDto = NewUserDto(EMAIL, PASSWORD, FIRSTNAME, LASTNAME)
    }

    init {
        val factory = UserFactory(Uuid(), ApiToken(), PasswordHasher())

        "UserFactory should invoke uuid generation" {
            val user = factory.createUser(newUserDto)

            user.uuid shouldNotBe null
        }

        "UserFactory should invoke apiKey generation" {
            val user = factory.createUser(newUserDto)

            user.apiKey shouldNotBe null
        }

        "UserFactory should use data from newUserDto" {
            val user = factory.createUser(newUserDto)

            user.firstName shouldBe FIRSTNAME
            user.lastName shouldBe LASTNAME
            user.email shouldBe EMAIL
        }

        "UserFactory should not generate id field" {
            val user = factory.createUser(newUserDto)

            user.id shouldBe null
        }

        "UserFactory should generate hash and salt" {
            val user = factory.createUser(newUserDto)

            user.passwordHash shouldNotBe null
            user.passwordSalt shouldNotBe null
        }
    }
}