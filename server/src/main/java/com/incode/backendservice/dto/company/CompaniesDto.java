package com.incode.backendservice.dto.company;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CompaniesDto {

    private CompanyResultWrapperDto resultWrapper;
    private List<CompanyExecutionResultDto> executionResults = new ArrayList<>();
}
