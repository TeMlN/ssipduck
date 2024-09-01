package com.server.ssipduck.domain.auth.service;

import com.server.ssipduck.domain.auth.dto.AuthTokenResponse;
import com.server.ssipduck.domain.auth.dto.KakaoUserInfo;
import com.server.ssipduck.domain.auth.property.AuthProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

import java.nio.charset.StandardCharsets;
import java.util.Collections;

@Service
@RequiredArgsConstructor
public class KakaoOauth2RegistrationService {

    private final AuthProperties authProperties;

    public KakaoUserInfo registrationKakaoUser(String code) {
        AuthTokenResponse token = getToken(code);
        return getUserInfo(token);
    }

    private AuthTokenResponse getToken(String code) {
        return WebClient.create()
                .post()
                .uri("https://kauth.kakao.com/oauth/token")
                .headers(httpHeaders -> {
                    httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
                    httpHeaders.setAcceptCharset(Collections.singletonList(StandardCharsets.UTF_8));
                })
                .bodyValue(tokenRequestBody(code))
                .retrieve()
                .bodyToMono(AuthTokenResponse.class)
                .block();
    }

    private KakaoUserInfo getUserInfo(AuthTokenResponse authTokenResponse) {
        return WebClient.create()
                .get()
                .uri("https://kapi.kakao.com/v2/user/me")
                .headers(httpHeaders -> {
                    httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
                    httpHeaders.setAcceptCharset(Collections.singletonList(StandardCharsets.UTF_8));
                    httpHeaders.add("Authorization", "Bearer " + authTokenResponse.getAccessToken());
                })
                .retrieve()
                .bodyToMono(KakaoUserInfo.class)
                .block();
    }

    private MultiValueMap<String, String> tokenRequestBody(String code) {
        MultiValueMap<String,String> body = new LinkedMultiValueMap<>();

        body.add("grant_type", "authorization_code");
        body.add("client_id", authProperties.getClientId());
        body.add("redirect_uri", authProperties.getRedirectUri());
        body.add("code", code);
        body.add("client_secret", authProperties.getClientSecret());

        return body;
    }
}
