package com.incode.backendservice.service.impl;

import com.incode.backendservice.domain.Verification;
import com.incode.backendservice.dto.verification.CreateVerificationDto;
import com.incode.backendservice.dto.verification.VerificationDto;
import com.incode.backendservice.exception.CustomException;
import com.incode.backendservice.exception.CustomExceptionKey;
import com.incode.backendservice.repository.VerificationRepository;
import com.incode.backendservice.service.api.VerificationService;
import com.incode.backendservice.service.mapper.VerificationServiceMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@RequiredArgsConstructor
@Service
public class VerificationServiceImpl implements VerificationService {

    private final VerificationRepository verificationRepository;
    private final VerificationServiceMapper mapper;

    @Transactional
    @Override
    public void addVerification(CreateVerificationDto dto) {

        Verification verification = mapper.fromCreateDtoToDomain(dto);

        verificationRepository.save(verification);
    }

    @Override
    public VerificationDto getByVerificationId(@NonNull String verificationId) {

        return verificationRepository.findByVerificationId(verificationId)
                .map(mapper::fromDomainToDto)
                .orElseThrow(() ->
                        new CustomException(CustomExceptionKey.NOT_FOUND,
                                String.format("Verification does not exist for verificationId:%s", verificationId)));
    }

    @Override
    public void validateVerificationId(String verificationId) {

        if (verificationRepository.existsByVerificationId(verificationId)) {
            log.warn("VerificationId '{}' already exists in system", verificationId);
            throw new CustomException(CustomExceptionKey.VERIFICATION_ALREADY_EXIST,
                    String.format("VerificationId '%s' already exist in system", verificationId));
        }

    }
}
