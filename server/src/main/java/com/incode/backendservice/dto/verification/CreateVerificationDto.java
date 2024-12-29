package com.incode.backendservice.dto.verification;


import com.incode.backendservice.dto.company.CompanyExecutionResultDto;
import lombok.Builder;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

@Builder
public record CreateVerificationDto(Map<String, Object> queryParameters, String verificationId,
                                    OffsetDateTime timestamp, String result, String source,
                                    List<CompanyExecutionResultDto> metadata) {
}

