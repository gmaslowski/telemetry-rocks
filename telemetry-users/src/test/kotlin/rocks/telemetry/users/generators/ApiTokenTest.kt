package rocks.telemetry.users.generators

import io.kotlintest.matchers.shouldBe
import io.kotlintest.specs.FreeSpec
import rocks.telemetry.users.generators.ApiToken

class ApiTokenTest : FreeSpec() {

    init {
        val apiToken = ApiToken()

        "ApiToken should create 12-chars long ApiToken" {
            val token = apiToken.generate()

            token.length shouldBe 12
            println(token)
        }
    }
}