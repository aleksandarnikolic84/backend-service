package com.incode.backendservice.facade.api;

import com.incode.backendservice.api.web.response.VerificationResponse;

public interface VerificationFacade {

    VerificationResponse getByVerificationId(String verificationId);
}
