package com.incode.backendservice.dto.company;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;


@Data
public class CompanyResultWrapperDto {

    private CompanyDataProviderData source;

    private CompanyExecutionStatusDto status;

    private List<CompanyDto> companies = new ArrayList<>();

    private Integer possibleMatches;
}
