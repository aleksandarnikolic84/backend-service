package com.incode.backendservice.facade.mapper;


import com.incode.backendservice.api.web.response.VerificationResponse;
import com.incode.backendservice.dto.verification.VerificationDto;
import org.springframework.stereotype.Component;


@Component
public class VerificationFacadeMapper {

    public VerificationResponse fromDtoToResponse(VerificationDto dto) {
        VerificationResponse result = new VerificationResponse();
        result.setVerificationId(dto.verificationId());
        result.setResult(dto.result());
        result.setId(dto.id());
        result.setSource(dto.source());
        result.setQueryText(dto.queryText());
        result.setTimestamp(dto.timestamp());
        return result;
    }
}
