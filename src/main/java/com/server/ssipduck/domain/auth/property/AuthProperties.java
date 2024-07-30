package com.server.ssipduck.domain.auth.property;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "oauth2.kakao")
public class AuthProperties {
    private final String clientId;
    private final String redirectUri;
    private final String clientSecret;

}
