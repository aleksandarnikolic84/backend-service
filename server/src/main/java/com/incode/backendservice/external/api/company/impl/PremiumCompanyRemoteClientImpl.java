package com.incode.backendservice.external.api.company.impl;

import com.incode.backendservice.dto.company.remote.CompanyRemoteResponse;
import com.incode.backendservice.dto.company.remote.CompanyStatusRemote;
import com.incode.backendservice.dto.company.remote.SingleCompanyRemote;
import com.incode.backendservice.external.api.company.api.CompanyClientRemote;
import com.incode.backendservice.external.api.company.mapper.PremiumCompanyRemoteMapper;
import com.incode.companyregistryservice.api.web.PremiumCompaniesApi;
import com.incode.companyregistryservice.api.web.dto.PremiumCompaniesDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientResponseException;

import java.util.List;

@SuppressWarnings(value = {"PMD.AvoidCatchingGenericException"})
@Slf4j
@RequiredArgsConstructor
@Component("premiumCompanyRemoteClient")
public class PremiumCompanyRemoteClientImpl implements CompanyClientRemote {

    private final PremiumCompaniesApi premiumCompaniesApi;
    private final PremiumCompanyRemoteMapper mapper;

    @Override
    public CompanyRemoteResponse findCompany(String query, Integer pageSize) {
        log.info("[Premium company] - Get data for parameter: {}, pageSize:{}", query, pageSize);
        CompanyRemoteResponse result = new CompanyRemoteResponse();
        try {
            PremiumCompaniesDto premiumCompanies = premiumCompaniesApi.findPremiumCompanies(query, 0, pageSize);
            if (premiumCompanies.getCompanies().isEmpty()) {
                result.setStatus(CompanyStatusRemote.NO_DATA);
                log.info("[Premium company] - No data for parameter: {}, pageSize:{}", query, pageSize);
                return result;
            }
            log.debug("[Premium company] - Fetched data: {}", premiumCompanies);
            List<SingleCompanyRemote> collected = premiumCompanies.getCompanies().stream()
                    .map(mapper::fromResponseToDto).toList();
            result.setCompanies(collected);
            result.setStatus(CompanyStatusRemote.SUCCESS);
            result.setTotalNumberOfElements(premiumCompanies.getPagination().getTotalElements());
            log.info("[Premium company] - Data are fetch successfully for query: {}", query);
        } catch (RestClientResponseException ex) {
            result.setStatus(CompanyStatusRemote.FAILED);
            result.setDescription(String
                    .format("Error message:%s. Http status:%s.", ex.getMessage(), ex.getStatusCode()));
            log.info("[Premium company] - Failed : {} with status code:{}", ex.getMessage(), ex.getStatusCode());
        } catch (Exception exception) {
            result.setStatus(CompanyStatusRemote.FAILED);
            result.setDescription(String.format("Error message:%s.", exception.getMessage()));
            log.info("[Premium company] - Failed : {}.", exception.getMessage());
        }
        return result;
    }
}
