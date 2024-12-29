package com.incode.backendservice.service.api;

import com.incode.backendservice.dto.company.CompaniesDto;
import org.springframework.lang.NonNull;

public interface CompanyService {

    CompaniesDto findByCin(@NonNull String cin, Integer upToSize);
}
