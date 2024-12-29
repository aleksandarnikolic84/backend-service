package com.incode.backendservice.facade.api;



import com.incode.backendservice.api.web.response.CompaniesResponse;

/**
 *
 */
public interface CompanyFacade {

    CompaniesResponse findCompany(String cin, String verificationId, Integer upToSize);
}
