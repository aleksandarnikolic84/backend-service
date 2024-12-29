package com.incode.backendservice.service.impl;

import com.incode.backendservice.dataprovider.company.api.CompanyDataProvider;
import com.incode.backendservice.dto.company.CompaniesDto;
import com.incode.backendservice.dto.company.CompanyDto;
import com.incode.backendservice.dto.company.CompanyResultWrapperDto;
import com.incode.backendservice.service.api.CompanyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;
import java.util.stream.Stream;


@Slf4j
@RequiredArgsConstructor
@Service
public class CompanyServiceImpl implements CompanyService {

    private final CompanyDataProvider companyDataProvider;

    private static final Integer DEFAULT_PAGE_SIZE = 30;

    @Override
    public CompaniesDto findByCin(@NonNull String cin, Integer upToSize) {

        int pageSize = (upToSize != null && upToSize > 0) ? Math.multiplyExact(upToSize, 2) : DEFAULT_PAGE_SIZE;
        CompaniesDto companiesDto = companyDataProvider.fetchData(cin, pageSize);
        CompanyResultWrapperDto resultWrapper = companiesDto.getResultWrapper();

        // Filter out inactive companies
        Stream<CompanyDto> dtoStream = resultWrapper.getCompanies().stream()
                .filter(CompanyDto::isActive);
        if (upToSize != null && !upToSize.equals(0)) {
            dtoStream = dtoStream.limit(upToSize);

        }
        resultWrapper.setCompanies(dtoStream.collect(Collectors.toList()));
        return companiesDto;
    }
}
