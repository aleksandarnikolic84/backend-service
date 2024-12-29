package com.incode.backendservice.web;

import com.incode.backendservice.api.web.CompaniesApi;
import com.incode.backendservice.api.web.response.CompaniesResponse;
import com.incode.backendservice.facade.api.CompanyFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class CompanyController implements CompaniesApi {

    private final CompanyFacade companyFacade;

    @Override
    public ResponseEntity<CompaniesResponse> findCompanies(String query, String verificationId, Integer upToSize) {

        return ResponseEntity.ok(companyFacade.findCompany(query, verificationId, upToSize));
    }
}
