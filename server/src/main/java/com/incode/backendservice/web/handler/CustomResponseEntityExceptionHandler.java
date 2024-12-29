package com.incode.backendservice.web.handler;


import com.incode.backendservice.api.web.response.ErrorResponse;
import com.incode.backendservice.exception.CustomException;
import com.incode.backendservice.exception.CustomExceptionKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
public class CustomResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {


    private final Map<CustomExceptionKey, HttpStatus> exceptionKeyHttpCode = Map
            .of(CustomExceptionKey.NOT_FOUND, HttpStatus.NOT_FOUND,
                    CustomExceptionKey.SERVICE_UNAVAILABLE, HttpStatus.SERVICE_UNAVAILABLE,
                    CustomExceptionKey.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR,
                    CustomExceptionKey.VERIFICATION_ALREADY_EXIST, HttpStatus.BAD_REQUEST);


    @ExceptionHandler(value = {CustomException.class})
    protected ResponseEntity<Object> handleCustomException(CustomException exception) {
        log.info("Custom exception caught:{}.", exception.getMessage());
        HttpStatus exceptionStatus = exceptionKeyHttpCode
                .getOrDefault(exception.getCustomExceptionKey(), HttpStatus.INTERNAL_SERVER_ERROR);
        return ResponseEntity.status(exceptionStatus)
                .body(new ErrorResponse()
                        .errorCode(exceptionStatus.name())
                        .error(exception.getMessage())
                        .timestamp(OffsetDateTime.now())
                        .reasons(exception.getParams()));
    }

    @ExceptionHandler(value = {Exception.class})
    protected ResponseEntity<Object> handleGenericError(Exception ex, WebRequest webRequest) {
        log.error("Unexpected error occurred. Message: {}.", ex.getMessage());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse()
                        .errorCode(HttpStatus.INTERNAL_SERVER_ERROR.name())
                        .error(ex.getMessage())
                        .timestamp(OffsetDateTime.now()));
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(@NonNull MethodArgumentNotValidException ex,
                                                                  @NonNull HttpHeaders headers,
                                                                  @NonNull HttpStatusCode status,
                                                                  @NonNull WebRequest request) {

        log.info("Validation error!", ex);
        final Map<String, String> map = ex.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(FieldError::getField, c -> c.getDefaultMessage()));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse()
                        .error("Validation error!")
                        .reasons(map)
                        .timestamp(OffsetDateTime.now())
                        .errorCode(HttpStatus.BAD_REQUEST.name()));
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex,
                                                                          @NonNull HttpHeaders headers,
                                                                          @NonNull HttpStatusCode status,
                                                                          @NonNull WebRequest request) {
        String error = ex.getParameterName() + " parameter is missing";
        log.error(error, ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse()
                        .error(error)
                        .errorCode(HttpStatus.BAD_REQUEST.name())
                        .timestamp(OffsetDateTime.now()));
    }
}
