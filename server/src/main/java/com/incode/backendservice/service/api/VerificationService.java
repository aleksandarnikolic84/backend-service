package com.incode.backendservice.service.api;

import com.incode.backendservice.dto.verification.CreateVerificationDto;
import com.incode.backendservice.dto.verification.VerificationDto;
import org.springframework.lang.NonNull;

public interface VerificationService {
    void addVerification(CreateVerificationDto dto);

    VerificationDto getByVerificationId(@NonNull String verificationId);

    void validateVerificationId(String verificationId);
}
