package com.incode.backendservice.dataprovider.company.mapper;


import com.incode.backendservice.dto.company.CompanyDto;
import com.incode.backendservice.dto.company.CompanyExecutionStatusDto;
import com.incode.backendservice.dto.company.remote.CompanyStatusRemote;
import com.incode.backendservice.dto.company.remote.SingleCompanyRemote;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CompanyDataHandlerMapper {

    public List<CompanyDto> fromRemoteToDto(List<SingleCompanyRemote> companies) {
        return companies.stream().map(it ->
                CompanyDto.builder()
                        .companyName(it.companyName())
                        .address(it.address())
                        .isActive(it.isActive())
                        .cin(it.cin())
                        .registrationDate(it.registrationDate())
                        .build()
        ).collect(Collectors.toList());
    }

    public CompanyExecutionStatusDto fromRemoteToDto(CompanyStatusRemote status) {

        return switch (status) {
            case FAILED -> CompanyExecutionStatusDto.FAILED;
            case NO_DATA -> CompanyExecutionStatusDto.NO_DATA;
            case SUCCESS -> CompanyExecutionStatusDto.SUCCESS;
        };
    }
}
