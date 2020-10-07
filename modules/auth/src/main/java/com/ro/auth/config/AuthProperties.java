package com.ro.auth.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

@Getter
@ConfigurationProperties(prefix = "auth")
public class AuthProperties {
    private final TokenInfo tokenInfo = new TokenInfo();
    private final OAuth2Info oauth2Info = new OAuth2Info();

    @Getter
    @Setter
    public static class TokenInfo {
        private String tokenSecret;
        private long tokenExpirationMs;
    }

    @Getter
    @Setter
    public static final class OAuth2Info {
        private List<String> authorizedRedirectUris = new ArrayList<>();
    }
}