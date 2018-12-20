package rocks.telemetry.users.generators

import org.springframework.stereotype.Component
import java.security.SecureRandom

@Component
class ApiToken {

    companion object {
        val AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"
        val SECURE_RANDOM = SecureRandom()
        val KEY_LENGTH = 12
    }

    fun generate(): String {
        val sb = StringBuilder(KEY_LENGTH)
        for (i in 0 until KEY_LENGTH)
            sb.append(AB[SECURE_RANDOM.nextInt(AB.length)])
        return sb.toString()
    }
}


