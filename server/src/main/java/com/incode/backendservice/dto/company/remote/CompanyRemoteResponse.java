package com.incode.backendservice.dto.company.remote;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CompanyRemoteResponse {

    private CompanyStatusRemote status;
    private String description;
    private Integer totalNumberOfElements;
    private List<SingleCompanyRemote> companies = new ArrayList<>();
}
