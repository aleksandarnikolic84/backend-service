package com.incode.backendservice.dto.verification;

import lombok.Builder;

import java.time.OffsetDateTime;
import java.util.Map;

@Builder
public record VerificationDto(Long id, String verificationId, Map<String, String> queryText,
                              OffsetDateTime timestamp, String source, String result) {
}
