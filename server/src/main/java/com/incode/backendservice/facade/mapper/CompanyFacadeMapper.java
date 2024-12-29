package com.incode.backendservice.facade.mapper;

import com.incode.backendservice.api.web.response.CompaniesResponse;
import com.incode.backendservice.api.web.response.CompanyResponse;
import com.incode.backendservice.api.web.response.CompanyResultsWrapperResponse;
import com.incode.backendservice.dto.company.CompaniesDto;
import com.incode.backendservice.dto.company.CompanyDto;
import com.incode.backendservice.dto.company.CompanyResultWrapperDto;
import com.incode.backendservice.dto.verification.CreateVerificationDto;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;


@Component
public class CompanyFacadeMapper {

    public CompaniesResponse fromDtoToResponse(String cin, String verificationId, CompaniesDto companiesDto) {
        CompaniesResponse companiesResponse = new CompaniesResponse();
        companiesResponse.setQuery(cin);
        companiesResponse.setVerificationId(verificationId);

        CompanyResultsWrapperResponse resultWrapperResponse = new CompanyResultsWrapperResponse();
        CompanyResultWrapperDto resultWrapper = companiesDto.getResultWrapper();
        if (resultWrapper.getCompanies() != null && !resultWrapper.getCompanies().isEmpty()) {
            resultWrapperResponse.setMatch(convertToResponse(resultWrapper.getCompanies().get(0)));
        }
        resultWrapperResponse.setPossibleMatches(resultWrapper.getPossibleMatches());
        List<CompanyResponse> companyResponses = resultWrapper.getCompanies()
                .stream()
                .skip(1)
                .map(this::convertToResponse)
                .toList();
        resultWrapperResponse.setOtherResults(companyResponses);
        companiesResponse.setResults(resultWrapperResponse);

        return companiesResponse;
    }

    private CompanyResponse convertToResponse(CompanyDto companyDto) {
        CompanyResponse result = new CompanyResponse();
        result.setAddress(companyDto.address());
        result.setCin(companyDto.cin());
        result.setCompanyName(companyDto.companyName());
        result.setIsActive(companyDto.isActive());
        result.setRegistrationDate(companyDto.registrationDate());

        return result;
    }

    public CreateVerificationDto createVerificationDto(String cin, String verificationId, Integer upToSize,
                                                       CompaniesDto companiesDto, String result) {

        CompanyResultWrapperDto resultWrapper = companiesDto.getResultWrapper();
        String sourceName = resultWrapper != null &&
                resultWrapper.getSource() != null
                ? resultWrapper.getSource().name()
                : null;

        return CreateVerificationDto.builder()
                .queryParameters(Map.of("cin", cin, "upToSize", upToSize))
                .verificationId(verificationId)
                .timestamp(OffsetDateTime.now())
                .metadata(companiesDto.getExecutionResults())
                .result(result)
                .source(sourceName)
                .build();
    }
}
