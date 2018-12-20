package rocks.telemetry.users.handler

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import rocks.telemetry.users.repo.UserRepository

@Component
class UserApiKeyVerifier @Autowired constructor(private val userRepository: UserRepository) {

    fun verifyKey(email: String, apiToken: String): Mono<Boolean> {
        return userRepository.findByEmail(email)
            .map { apiToken == it.apiKey }
            .switchIfEmpty(Mono.just(false))
    }
}
