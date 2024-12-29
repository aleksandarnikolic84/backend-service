package com.incode.backendservice.web;

import com.incode.backendservice.api.web.VerificationApi;
import com.incode.backendservice.api.web.response.VerificationResponse;
import com.incode.backendservice.facade.api.VerificationFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@RestController
public class VerificationController implements VerificationApi {

    private final VerificationFacade verificationFacade;

    @Override
    public ResponseEntity<VerificationResponse> getVerification(String verificationId) {

        return ResponseEntity.ok(verificationFacade.getByVerificationId(verificationId));
    }
}
