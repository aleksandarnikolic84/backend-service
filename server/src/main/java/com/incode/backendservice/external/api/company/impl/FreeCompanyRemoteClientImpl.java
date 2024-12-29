package com.incode.backendservice.external.api.company.impl;

import com.incode.backendservice.dto.company.remote.CompanyRemoteResponse;
import com.incode.backendservice.dto.company.remote.CompanyStatusRemote;
import com.incode.backendservice.dto.company.remote.SingleCompanyRemote;
import com.incode.backendservice.external.api.company.api.CompanyClientRemote;
import com.incode.backendservice.external.api.company.mapper.FreeCompanyRemoteMapper;
import com.incode.companyregistryservice.api.web.FreeCompaniesApi;
import com.incode.companyregistryservice.api.web.dto.FreeCompaniesDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientResponseException;

import java.util.List;


@SuppressWarnings(value = {"PMD.AvoidCatchingGenericException"})
@Slf4j
@RequiredArgsConstructor
@Component("freeCompanyRemoteClient")
public class FreeCompanyRemoteClientImpl implements CompanyClientRemote {

    private final FreeCompaniesApi freeCompaniesApi;
    private final FreeCompanyRemoteMapper mapper;


    @Override
    public CompanyRemoteResponse findCompany(String query, Integer pageSize) {
        log.info("[Free company] - Get data for parameter: {}, pageSize:{}", query, pageSize);
        CompanyRemoteResponse result = new CompanyRemoteResponse();
        try {
            FreeCompaniesDto primaryCompanies = freeCompaniesApi.findPrimaryCompanies(query, 0, pageSize);
            if (primaryCompanies.getCompanies().isEmpty()) {
                result.setStatus(CompanyStatusRemote.NO_DATA);
                log.info("[Free company] - No data for parameter: {}, pageSize:{}", query, pageSize);
                return result;
            }
            log.debug("[Free company] - Fetched data: {}", primaryCompanies);

            List<SingleCompanyRemote> collected = primaryCompanies.getCompanies().stream()
                    .map(mapper::fromResponseToDto).toList();
            result.setCompanies(collected);
            result.setStatus(CompanyStatusRemote.SUCCESS);
            result.setTotalNumberOfElements(primaryCompanies.getPagination().getTotalElements());

            log.info("[Free company] - Data are fetch successfully for query: {}", query);
        } catch (RestClientResponseException ex) {
            //TODO For 4xx exception should check body in purpose of message
            result.setStatus(CompanyStatusRemote.FAILED);
            result.setDescription(String
                    .format("Error message:%s. Http status:%s.", ex.getMessage(), ex.getStatusCode()));
            log.info("[Free company] - Failed : {} with status code:{}", ex.getMessage(), ex.getStatusCode());
        } catch (Exception exception) {
            result.setStatus(CompanyStatusRemote.FAILED);
            result.setDescription(String.format("Error message:%s.", exception.getMessage()));
            log.info("[Free company] - Failed : {}.", exception.getMessage());
        }
        return result;
    }
}
