package com.incode.backendservice.inventory;

import com.incode.backendservice.dto.company.CompaniesDto;
import com.incode.backendservice.dto.company.CompanyDto;
import com.incode.backendservice.dto.company.CompanyExecutionStatusDto;
import com.incode.backendservice.dto.company.CompanyResultWrapperDto;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CompanyDtoInventory {

    @NotNull
    public static CompaniesDto createCompaniesDto(int activeCompanies, int inactiveCompanies, CompanyExecutionStatusDto status) {
        CompaniesDto companiesDto = new CompaniesDto();
        CompanyResultWrapperDto companyResultWrapperDto = new CompanyResultWrapperDto();
        companyResultWrapperDto.setStatus(status);
        companyResultWrapperDto.setCompanies(createCompanies(activeCompanies, inactiveCompanies));
        companiesDto.setResultWrapper(companyResultWrapperDto);
        return companiesDto;
    }

    private static List<CompanyDto> createCompanies(int numActive, int numInactive) {
        // Create active companies
        List<CompanyDto> companies = new ArrayList<>();
        for (int i = 0; i < numActive; i++) {
            companies.add(CompanyDto.builder()
                    .isActive(true)
                    .build());
        }
        // Create inactive companies
        for (int i = 0; i < numInactive; i++) {
            companies.add(CompanyDto.builder()
                    .isActive(false)
                    .build());
        }
        return companies;
    }
}
