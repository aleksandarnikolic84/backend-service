package com.incode.backendservice.dataprovider.company.handler.api;

import com.incode.backendservice.dataprovider.company.handler.CompanyHandlerType;
import com.incode.backendservice.dataprovider.company.mapper.CompanyDataHandlerMapper;
import com.incode.backendservice.dto.company.CompaniesDto;
import com.incode.backendservice.dto.company.CompanyDataProviderData;
import com.incode.backendservice.dto.company.CompanyExecutionResultDto;
import com.incode.backendservice.dto.company.CompanyResultWrapperDto;
import com.incode.backendservice.dto.company.remote.CompanyRemoteResponse;
import com.incode.backendservice.dto.company.remote.CompanyStatusRemote;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public abstract class CompanyDataHandler {

    private CompanyDataHandler nextDataProvider;

    private final CompanyDataHandlerMapper mapper;

    /**
     * Abstract method which is supplier for company data
     */
    protected abstract CompanyRemoteResponse companyDataSupplier(String cin, Integer pageSize);

    public abstract CompanyHandlerType getType();

    public void setNextDataProvider(CompanyDataHandler nextDataProvider) {
        this.nextDataProvider = nextDataProvider;
    }

    public CompaniesDto fetchData(String cin, Integer pageSize) {

        CompaniesDto result = new CompaniesDto();

        CompanyRemoteResponse companyData = companyDataSupplier(cin, pageSize);

        CompanyExecutionResultDto metadata = CompanyExecutionResultDto.builder()
                .source(getDataProviderDto(getType()))
                .status(mapper.fromRemoteToDto(companyData.getStatus()))
                .description(companyData.getDescription())
                .build();

        result.getExecutionResults().add(metadata);

        CompanyResultWrapperDto resultWrapper = new CompanyResultWrapperDto();
        result.setResultWrapper(resultWrapper);
        resultWrapper.setStatus(mapper.fromRemoteToDto(companyData.getStatus()));
        resultWrapper.setPossibleMatches(0);
        if (companyData.getStatus() == CompanyStatusRemote.SUCCESS) {
            resultWrapper.setSource(getDataProviderDto(getType()));
            resultWrapper.setPossibleMatches(companyData.getTotalNumberOfElements());
            resultWrapper.setCompanies(mapper.fromRemoteToDto(companyData.getCompanies()));

            //Call next handler from chain if exists
        } else if (hasNextStep()) {
            CompaniesDto companiesDto = getNextDataProvider().fetchData(cin, pageSize);
            result.getExecutionResults().addAll(companiesDto.getExecutionResults());
            result.setResultWrapper(companiesDto.getResultWrapper());
        }
        return result;
    }

    private CompanyDataHandler getNextDataProvider() {
        return nextDataProvider;
    }

    private CompanyDataProviderData getDataProviderDto(CompanyHandlerType type) {
        return switch (type) {
            case FREE -> CompanyDataProviderData.FREE;
            case PREMIUM -> CompanyDataProviderData.PREMIUM;
        };
    }

    private boolean hasNextStep() {
        return nextDataProvider != null;
    }
}
