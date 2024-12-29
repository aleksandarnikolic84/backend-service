package com.incode.backendservice.external.api.company.mapper;


import com.incode.backendservice.dto.company.remote.SingleCompanyRemote;
import com.incode.companyregistryservice.api.web.dto.PremiumCompanyDto;
import org.springframework.stereotype.Component;

@Component
public class PremiumCompanyRemoteMapper {

    public SingleCompanyRemote fromResponseToDto(PremiumCompanyDto it) {
        return SingleCompanyRemote.builder()
                .companyName(it.getCompanyName())
                .cin(it.getCompanyIdentificationNumber())
                .address(it.getCompanyFullAddress())
                .registrationDate(it.getRegistrationDate())
                .isActive(it.getIsActive())
                .build();
    }
}
