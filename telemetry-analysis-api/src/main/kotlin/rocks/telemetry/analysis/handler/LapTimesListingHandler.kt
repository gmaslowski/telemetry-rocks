package rocks.telemetry.analysis.handler

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono
import reactor.core.publisher.toMono
import rocks.telemetry.analysis.api.LapTimeDto
import rocks.telemetry.analysis.repo.LapTimeRepository

@Component
class LapTimesListingHandler @Autowired constructor(val lapTimeRepository: LapTimeRepository) {

    fun getAllLapTimes(serverRequest: ServerRequest): Mono<ServerResponse> {
        return serverRequest
            .headers()
            .header("oauth_token")[0]
            .toMono()
            .flatMap { token ->
                val authBody = RestTemplate().getForObject("http://telemetry-auth:8082/user?access_token=" + token, AuthBody::class.java)
                ServerResponse
                    .ok()
                    .contentType(APPLICATION_JSON)
                    .body(lapTimeRepository.findByUser(authBody.principal).map { it.asDto() }, LapTimeDto::class.java)
            }
            .switchIfEmpty(NOT_FOUND)
    }
}