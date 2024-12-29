package com.incode.backendservice.external.api.company.mapper;


import com.incode.backendservice.dto.company.remote.SingleCompanyRemote;
import com.incode.companyregistryservice.api.web.dto.FreeCompanyDto;
import org.springframework.stereotype.Component;

@Component
public class FreeCompanyRemoteMapper {


    public SingleCompanyRemote fromResponseToDto(FreeCompanyDto freeCompanyDto) {
        return SingleCompanyRemote.builder()
                .companyName(freeCompanyDto.getCompanyName())
                .cin(freeCompanyDto.getCin())
                .address(freeCompanyDto.getAddress())
                .registrationDate(freeCompanyDto.getRegistrationDate())
                .isActive(freeCompanyDto.getIsActive())
                .build();
    }
}
