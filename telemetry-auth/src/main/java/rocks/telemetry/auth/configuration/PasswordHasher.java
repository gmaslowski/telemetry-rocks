package rocks.telemetry.auth.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

@Component
public class PasswordHasher {

    private static final Logger log = LoggerFactory.getLogger(PasswordHasher.class);

    private static final int ITERATIONS_COUNT = 1000;
    private static final String PBKDF2_ALGORITHM = "PBKDF2WithHmacSHA1";
    private static final int HASH_BYTES = 24;

    public String generateHash(String password, String salt) {

        PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), fromHex(salt), ITERATIONS_COUNT, HASH_BYTES * 8);

        try {
            SecretKeyFactory skf = SecretKeyFactory.getInstance(PBKDF2_ALGORITHM);
            return toHex(skf.generateSecret(spec).getEncoded());
        } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
            log.warn("Error while pass hashing: ", e);
            return null;
        }
    }

    private String toHex(byte[] byteArray) {
        BigInteger bi = new BigInteger(1, byteArray);
        String hex = bi.toString(16);
        Integer paddingLength = byteArray.length * 2 - hex.length();
        if (paddingLength > 0)
            return String.format("%0" + paddingLength + "d", 0) + hex;
        else
            return hex;
    }

    private static byte[] fromHex(String hex) {
        byte[] binary = new byte[hex.length() / 2];
        for (int i = 0; i < binary.length; i++) {
            binary[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
        }
        return binary;
    }
}
