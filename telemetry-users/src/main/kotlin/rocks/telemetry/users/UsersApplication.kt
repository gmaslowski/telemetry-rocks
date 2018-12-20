package rocks.telemetry.users

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories


@SpringBootApplication
@EnableMongoRepositories
class UsersApplication

fun main(args: Array<String>) {
    SpringApplication.run(UsersApplication::class.java, *args)
}
