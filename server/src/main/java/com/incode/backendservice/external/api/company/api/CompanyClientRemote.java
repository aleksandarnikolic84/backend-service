package com.incode.backendservice.external.api.company.api;

import com.incode.backendservice.dto.company.remote.CompanyRemoteResponse;

/**
 * Interface which hides complexity of finding company data.
 * With this abstraction data are normalised and has common interface.
 * <p>
 * This layer does not throw exception if third party not available but keep those data in {@link CompanyRemoteResponse}
 * <p>
 * Two implementation are available
 * {@link com.incode.backendservice.external.api.company.impl.FreeCompanyRemoteClientImpl} &
 * {@link com.incode.backendservice.external.api.company.impl.PremiumCompanyRemoteClientImpl}
 */
public interface CompanyClientRemote {

    CompanyRemoteResponse findCompany(String query, Integer pageSize);
}
