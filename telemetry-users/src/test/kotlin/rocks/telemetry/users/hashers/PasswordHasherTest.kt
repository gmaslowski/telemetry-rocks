package rocks.telemetry.users.hashers

import io.kotlintest.matchers.shouldBe
import io.kotlintest.specs.FreeSpec

class PasswordHasherTest : FreeSpec() {

    companion object {
        val PASSWORD = "myPass"
    }

    init {
        val passwordHasher = PasswordHasher()

        "PasswordHasher should create 48-chars long hash and salt" {
            val password = passwordHasher.hash(PASSWORD)

            password.hash.length shouldBe 48
            password.salt.length shouldBe 48
        }
    }
}