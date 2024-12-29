package com.incode.backendservice.service.mapper;

import com.incode.backendservice.domain.QueryParams;
import com.incode.backendservice.domain.Verification;
import com.incode.backendservice.dto.verification.CreateVerificationDto;
import com.incode.backendservice.dto.verification.VerificationDto;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class VerificationServiceMapper {

    public VerificationDto fromDomainToDto(Verification domain) {
        return VerificationDto.builder()
                .verificationId(domain.getVerificationId())
                .id(domain.getId())
                .queryText(convertQueryParams(domain.getQueryText()))
                .result(domain.getResult())
                .source(domain.getSource())
                .timestamp(domain.getTimestamp())
                .build();
    }

    private Map<String, String> convertQueryParams(QueryParams queryText) {
        return queryText.getParams().entrySet().stream()
                .filter(entry -> entry.getValue() != null) // Exclude null values
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> String.valueOf(entry.getValue())
                ));
    }

    public Verification fromCreateDtoToDomain(CreateVerificationDto dto) {
        Verification result = new Verification();
        result.setVerificationId(dto.verificationId());
        result.setQueryText(new QueryParams(dto.queryParameters()));
        result.setTimestamp(dto.timestamp());
        result.setSource(dto.source());
        result.setCreatedTime(OffsetDateTime.now());
        result.setResult(dto.result());
        return result;
    }
}
