package rocks.telemetry.analysis.route

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.web.reactive.function.server.router
import rocks.telemetry.analysis.handler.LapTimesListingHandler

@Configuration
class LapTimeRoutes @Autowired constructor(private val lapTimesListingHandler: LapTimesListingHandler) {

    @Bean
    fun lapTimeRouter() = router {
        (accept(APPLICATION_JSON) and "/lapTime").nest {
            GET("/", lapTimesListingHandler::getAllLapTimes)
        }
    }
}