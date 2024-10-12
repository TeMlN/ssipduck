package com.server.ssipduck.global.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

public class ResponseHandler {

    public static ResponseEntity<Object> of(Object result) {
        Map<String, Object> map = new HashMap<>();
        map.put("items", result);
        map.put("success", true);

        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    public static ResponseEntity<Object> success() {
        Map<String, Object> map = new HashMap<>();
        map.put("success", true);

        return new ResponseEntity<>(map, HttpStatus.OK);
    }
}
