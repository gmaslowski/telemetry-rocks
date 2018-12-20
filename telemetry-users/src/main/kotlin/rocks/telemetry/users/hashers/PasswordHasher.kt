package rocks.telemetry.users.hashers

import org.springframework.stereotype.Component
import java.math.BigInteger
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import java.security.spec.InvalidKeySpecException
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec


@Component
class PasswordHasher {

    companion object {

        val PBKDF2_ALGORITHM = "PBKDF2WithHmacSHA1"
        val PBKDF2_ITERATIONS = 1000

        val SALT_BYTES = 24
        val HASH_BYTES = 24

        data class Password(val hash: String, val salt: String)
    }

    fun hash(password: String): Password {

        val random = SecureRandom()
        val salt = ByteArray(SALT_BYTES)
        random.nextBytes(salt)

        val hash = pbkdf2(password.toCharArray(), salt, PBKDF2_ITERATIONS, HASH_BYTES)

        return Password(hash.toHex(), salt.toHex())
    }

    @Throws(NoSuchAlgorithmException::class, InvalidKeySpecException::class)
    private fun pbkdf2(password: CharArray, salt: ByteArray, iterations: Int, bytes: Int): ByteArray {
        val spec = PBEKeySpec(password, salt, iterations, bytes * 8)
        val skf = SecretKeyFactory.getInstance(PBKDF2_ALGORITHM)

        return skf.generateSecret(spec).encoded
    }
}

fun ByteArray.toHex(): String {
    val bi = BigInteger(1, this)
    val hex = bi.toString(16)

    val paddingLength = this.size * 2 - hex.length
    return if (paddingLength > 0)
        String.format("%0" + paddingLength + "d", 0) + hex
    else
        hex
}