package rocks.telemetry.auth.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class TelemetryUsersPasswordEncoder implements PasswordEncoder {

    private static final Logger log = LoggerFactory.getLogger(PasswordEncoder.class);

    @Autowired
    private PasswordHasher passwordHasher;

    @Override
    public String encode(CharSequence charSequence) {
        return null;
    }

    @Override
    public boolean matches(CharSequence providedPassword, String hashAndSalt) {

        log.error(providedPassword + "   " + hashAndSalt);

        String[] split = hashAndSalt.split(":");
        String passwordHash = split[0];
        String passwordSalt = split[1];

        String generated = passwordHasher.generateHash(providedPassword.toString(), passwordSalt);

        return passwordHash.equals(generated);
    }
}
