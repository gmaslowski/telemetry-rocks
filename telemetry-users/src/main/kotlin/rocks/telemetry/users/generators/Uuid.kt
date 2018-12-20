package rocks.telemetry.users.generators

import org.springframework.stereotype.Component
import java.util.UUID

@Component
class Uuid {

    fun generate(): String{
        return UUID.randomUUID().toString()
    }
}
