package com.server.ssipduck.global.exception.response;

import com.server.ssipduck.global.exception.ExceptionCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ExceptionResponseEntity {

    private final String code;
    private String message;
    private Boolean success;

    public static ExceptionResponseEntity of(ExceptionCode exceptionCode) {
        return new ExceptionResponseEntity(exceptionCode.getCode(), exceptionCode.getMessage(), exceptionCode.getSuccess());
    }

}
