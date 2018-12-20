package rocks.telemetry.analysis.repo

import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import rocks.telemetry.analysis.entity.LapTime

@Repository
interface LapTimeRepository : ReactiveMongoRepository<LapTime, String> {

    fun findByUser(userEmail: String): Flux<LapTime>
}