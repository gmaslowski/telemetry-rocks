package rocks.telemetry.users.handler

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono
import reactor.core.publisher.toMono
import rocks.telemetry.users.repo.UserRepository

@Component
class UserListingHandler @Autowired constructor(val userRepository: UserRepository) {

    fun getUserByEmail(serverRequest: ServerRequest): Mono<ServerResponse> {
        return serverRequest
            .pathVariable("email")
            .toMono()
            .flatMap {
                userRepository.findByEmail(it)
            }
            .flatMap {
                ServerResponse
                    .ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromObject(it.asUserWithPasswordDto()))
            }
            .switchIfEmpty(NOT_FOUND)
    }
}