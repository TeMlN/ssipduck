package com.server.ssipduck.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ExceptionCode {

    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "TOKEN-0", "Token is expired.", false),
    TOKEN_NOT_MATCHED(HttpStatus.BAD_REQUEST, "TOKEN-1", "Token is wrong or have problem.", false),
    TOKEN_INVALID(HttpStatus.FORBIDDEN, "TOKEN-2", "Token is spoiled.", false),

    UNKNOWN_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,"UNKNOWN", "Unknown error, check log", false);

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
    private final Boolean success;
}
