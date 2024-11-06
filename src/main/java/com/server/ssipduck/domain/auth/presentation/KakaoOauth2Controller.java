package com.server.ssipduck.domain.auth.presentation;

import com.server.ssipduck.domain.auth.service.KakaoOauth2RegistrationService;
import com.server.ssipduck.global.response.ResponseHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class KakaoOauth2Controller {

    private final KakaoOauth2RegistrationService kakaoOauth2RegistrationService;

    @RequestMapping(method = RequestMethod.GET, value = "v1/oauth/kakao")
    private ResponseEntity<?> registerByKakao(@RequestParam String code) {
        return ResponseHandler
                .of(kakaoOauth2RegistrationService.registrationKakaoUser(code));
    }
}
