package com.incode.backendservice.facade.impl;

import com.incode.backendservice.api.web.response.VerificationResponse;
import com.incode.backendservice.dto.verification.VerificationDto;
import com.incode.backendservice.facade.api.VerificationFacade;
import com.incode.backendservice.facade.mapper.VerificationFacadeMapper;
import com.incode.backendservice.service.api.VerificationService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;



@Slf4j
@RequiredArgsConstructor
@Component
public class VerificationFacadeImpl implements VerificationFacade {

    private final VerificationService verificationService;
    private final VerificationFacadeMapper mapper;

    @Override
    public VerificationResponse getByVerificationId(String verificationId) {
        log.info("Find verification by verificationId:{}", verificationId);
        VerificationDto dto = verificationService.getByVerificationId(verificationId);
        return mapper.fromDtoToResponse(dto);
    }
}
