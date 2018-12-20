package rocks.telemetry.users.repo

import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono
import rocks.telemetry.users.entity.User

@Repository
interface UserRepository : ReactiveMongoRepository<User, String> {
    fun findByUuid(uuid: String): Mono<User>
    fun findByEmail(email: String): Mono<User>
}