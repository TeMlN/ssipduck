package com.server.ssipduck.domain.auth.service;

import com.server.ssipduck.domain.auth.dto.AuthTokenResponse;
import com.server.ssipduck.domain.auth.dto.KakaoUserInfo;
import com.server.ssipduck.domain.auth.dto.UserIdDto;
import com.server.ssipduck.domain.auth.property.AuthProperties;
import com.server.ssipduck.domain.user.entity.Role;
import com.server.ssipduck.domain.user.entity.User;
import com.server.ssipduck.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class KakaoOauth2RegistrationService {

    private final AuthProperties authProperties;
    private final UserRepository userRepository;

    private static final String KAKAO_TOKEN_URL = "https://kauth.kakao.com/oauth/token";
    private static final String KAKAO_USER_INFO_URL = "https://kapi.kakao.com/v2/user/me";

    public UserIdDto registrationKakaoUser(String code) {
        AuthTokenResponse token = getToken(code);
        KakaoUserInfo userInfo = getUserInfo(token);

        /**
         * Kakao Oauth2를 사용해 UserInfo를 받아오면 이를 이용해 신규 등록하는 유저인지, 올드 유저 인지에 대한 분기처리
         * 올드 유저라면 User의 정보를 갱신하고 해당 유저의 ID 반환
         * 신규 유저라면 User정보를 저장하고 해당 유저의 ID 반환
         */
        return isNRU(userInfo.getId())
                .map(existingUser -> {
                    existingUser.modifyProfileImage(userInfo.getProperties().getProfileImage());
                    return new UserIdDto(existingUser.getId());
                })
                .orElseGet(() -> new UserIdDto(registrationNewUser(userInfo).getId()));
    }

    private AuthTokenResponse getToken(String code) {
        return WebClient.create()
                .post()
                .uri(KAKAO_TOKEN_URL)
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
                .uri(KAKAO_USER_INFO_URL)
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

    /**
     * 원래 가입돼 있는 유저가 있는지 확인하는 용도의 매서드
     * @param authId
     * @return
     */
    private Optional<User> isNRU(Long authId) {
        return userRepository.findByAuthId(authId);
    }

    private User registrationNewUser(KakaoUserInfo userInfo) {
        return userRepository.save(
                new User(
                        null,
                        userInfo.getProperties().getNickname(),
                        userInfo.getId(),
                        userInfo.getProperties().getProfileImage(),
                        Collections.singletonList(Role.ROLE_PERSONAL)
                ));
    }

}
