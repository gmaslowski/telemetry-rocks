package rocks.telemetry.users.handler


import com.nhaarman.mockito_kotlin.eq
import com.nhaarman.mockito_kotlin.whenever
import io.kotlintest.matchers.shouldBe
import io.kotlintest.mock.mock
import io.kotlintest.specs.FreeSpec
import reactor.core.publisher.Mono
import rocks.telemetry.users.entity.User
import rocks.telemetry.users.repo.UserRepository

class UserApiKeyVerifierTest : FreeSpec() {

    companion object {
        val repository = mock<UserRepository>()

        val EMAIL = "email"
        val APIKEY = "apiKey"
    }

    init {
        val verifier = UserApiKeyVerifier(userRepository = repository)

        "UserUserApiKeyVerifier should respond with TRUE if email and apiKey match" {
            whenever(repository.findByEmail(eq(EMAIL))).thenReturn(Mono.just(User(apiKey = APIKEY)))

            verifier.verifyKey(EMAIL, APIKEY).block() shouldBe true
        }

        "UserUserApiKeyVerifier should respond with FALSE if email and apiKey don't match" {
            whenever(repository.findByEmail(eq(EMAIL))).thenReturn(Mono.just(User(apiKey = "other")))

            verifier.verifyKey(EMAIL, APIKEY).block() shouldBe false
        }

        "UserUserApiKeyVerifier should respond with FALSE if user for email does not exist" {
            whenever(repository.findByEmail(eq(EMAIL))).thenReturn(Mono.empty())

            verifier.verifyKey(EMAIL, APIKEY).block() shouldBe false
        }
    }
}