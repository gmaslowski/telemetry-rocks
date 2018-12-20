package rocks.telemetry.auth.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

@Component
public class TelemetryUsersClientDetailsService implements ClientDetailsService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${telemetry.users.url}")
    private String usersServiceUrl;

    @Override
    public ClientDetails loadClientByClientId(String email) throws ClientRegistrationException {
        UserWithPasswordDto userWithPasswordDto = restTemplate.getForObject(usersServiceUrl + email, UserWithPasswordDto.class);

        BaseClientDetails baseClientDetails = new BaseClientDetails();
        baseClientDetails.setClientId(email);
        baseClientDetails.setClientSecret(userWithPasswordDto.passwordHash + ":" + userWithPasswordDto.passwordSalt);
        baseClientDetails.setAuthorizedGrantTypes(singletonList("client_credentials"));
        baseClientDetails.setScope(asList("resource-server-read", "resource-server-write"));

        return baseClientDetails;
    }
}
