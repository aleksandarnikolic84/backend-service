package com.incode.backendservice.exception;

import lombok.Getter;

import java.util.Map;

@Getter
public class CustomException extends RuntimeException {

    private final CustomExceptionKey customExceptionKey;

    private final Map<String, String> params;

    public CustomException(CustomExceptionKey exceptionKey) {
        this.customExceptionKey = exceptionKey;
        this.params = Map.of();
    }

    public CustomException(CustomExceptionKey exceptionKey, String message) {
        this(exceptionKey, message, Map.of());
    }


    public CustomException(CustomExceptionKey exceptionKey, String message, Map<String, String> params) {
        super(message);
        this.customExceptionKey = exceptionKey;
        this.params = params;
    }
}
