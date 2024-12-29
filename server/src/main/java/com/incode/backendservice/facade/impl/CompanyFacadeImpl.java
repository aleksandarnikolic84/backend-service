package com.incode.backendservice.facade.impl;


import com.incode.backendservice.api.web.response.CompaniesResponse;
import com.incode.backendservice.dto.company.CompaniesDto;
import com.incode.backendservice.dto.company.CompanyExecutionStatusDto;
import com.incode.backendservice.dto.verification.CreateVerificationDto;
import com.incode.backendservice.exception.CustomException;
import com.incode.backendservice.exception.CustomExceptionKey;
import com.incode.backendservice.facade.api.CompanyFacade;
import com.incode.backendservice.facade.mapper.CompanyFacadeMapper;
import com.incode.backendservice.service.api.CompanyService;
import com.incode.backendservice.service.api.VerificationService;
import com.incode.backendservice.util.MessageParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class CompanyFacadeImpl implements CompanyFacade {

    private final CompanyService companyService;
    private final VerificationService verificationService;
    private final CompanyFacadeMapper companyFacadeMapper;
    private final MessageParser messageParser;


    @Override
    public CompaniesResponse findCompany(String cin, String verificationId, Integer upToSize) {

        verificationService.validateVerificationId(verificationId);

        CompaniesDto companiesDto = companyService.findByCin(cin, upToSize);
        CompaniesResponse response = companyFacadeMapper.fromDtoToResponse(cin, verificationId, companiesDto);
        String executionResult = messageParser.writeValueAsString(response.getResults());
        CreateVerificationDto verificationDto = companyFacadeMapper
                .createVerificationDto(cin, verificationId, upToSize, companiesDto, executionResult);
        verificationService.addVerification(verificationDto);

        if (companiesDto.getResultWrapper().getStatus() == CompanyExecutionStatusDto.FAILED) {
            throw new CustomException(CustomExceptionKey.SERVICE_UNAVAILABLE,
                    "All third party providers are not available");
        }
        return response;
    }


}
