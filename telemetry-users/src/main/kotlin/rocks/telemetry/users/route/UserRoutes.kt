package rocks.telemetry.users.route

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.web.reactive.function.server.router
import rocks.telemetry.users.handler.UserApiKeyVerificationHandler
import rocks.telemetry.users.handler.UserCreationHandler
import rocks.telemetry.users.handler.UserListingHandler

@Configuration
class UserRoutes @Autowired constructor(private val userCreationHandler: UserCreationHandler,
                                        private val userListingHandler: UserListingHandler,
                                        private val userApiKeyVerificationHandler: UserApiKeyVerificationHandler) {

    @Bean
    fun userRouter() = router {
        (accept(APPLICATION_JSON) and "/user").nest {
            POST("/", userCreationHandler::addUser)
            GET("/withPassword/{email}", userListingHandler::getUserByEmail)
            POST("/verifyApiKey/{email}", userApiKeyVerificationHandler::verifyApiKey)
        }
    }
}