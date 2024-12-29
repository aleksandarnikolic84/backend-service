package com.incode.backendservice.util;

import com.incode.backendservice.exception.CustomException;
import com.incode.backendservice.exception.CustomExceptionKey;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


@SuppressWarnings(value = {"PMD.PreserveStackTrace"})
@Slf4j
@RequiredArgsConstructor
@Component
public class MessageParser {

    private final ObjectMapper objectMapper;

    public String writeValueAsString(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException ex) {
            log.error("Json parsing issue. Exception:{}.", ex.getMessage(), ex);
            throw new CustomException(CustomExceptionKey.INTERNAL_SERVER_ERROR,
                    String.format("Json result parsing issue. Message: %s", ex.getMessage()));
        }

    }

}
