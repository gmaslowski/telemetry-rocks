package rocks.telemetry.users.handler

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono
import reactor.core.publisher.toMono
import rocks.telemetry.users.api.ApiKeyVerificationDto

@Component
class UserApiKeyVerificationHandler @Autowired constructor(private val userApiKeyVerifier: UserApiKeyVerifier) {

    fun verifyApiKey(serverRequest: ServerRequest): Mono<ServerResponse> {

        return serverRequest
            .pathVariable("email")
            .toMono()
            .zipWith(serverRequest.bodyToMono(ApiKeyVerificationDto::class.java))
            .flatMap { userApiKeyVerifier.verifyKey(it.t1, it.t2.apiKey) }
            .flatMap {
                ServerResponse
                    .ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromObject(it.asDto()))
            }
            .switchIfEmpty(NOT_FOUND)
    }
}