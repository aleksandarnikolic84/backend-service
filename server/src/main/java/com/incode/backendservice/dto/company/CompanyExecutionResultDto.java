package com.incode.backendservice.dto.company;

import lombok.Builder;
import lombok.Data;


@Builder
@Data
public class CompanyExecutionResultDto {

    private CompanyDataProviderData source;
    private CompanyExecutionStatusDto status;
    private String description;
}
