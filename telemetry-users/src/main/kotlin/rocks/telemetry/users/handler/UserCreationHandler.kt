package rocks.telemetry.users.handler

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.BodyInserters.fromObject
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono
import rocks.telemetry.users.api.NewUserDto
import rocks.telemetry.users.repo.UserRepository

@Component
class UserCreationHandler @Autowired constructor(val userFactory: UserFactory,
                                                 val userRepository: UserRepository) {

    fun addUser(serverRequest: ServerRequest): Mono<ServerResponse> {

        return serverRequest
            .bodyToMono(NewUserDto::class.java)
            .flatMap {
                val newUser = userFactory.createUser(it)
                userRepository.save(newUser)
            }
            .flatMap {
                ServerResponse
                    .ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(fromObject(it.asDto()))
            }
            .switchIfEmpty(NOT_FOUND)
    }
}