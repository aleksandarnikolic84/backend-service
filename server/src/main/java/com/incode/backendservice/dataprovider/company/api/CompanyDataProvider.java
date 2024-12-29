package com.incode.backendservice.dataprovider.company.api;

import com.incode.backendservice.dto.company.CompaniesDto;

/**
 * Interface which hide complexity of fetching data.
 * Implementation load all {@link com.incode.backendservice.dataprovider.company.handler.api.CompanyDataHandler}
 * and put in chain for execution
 */
public interface CompanyDataProvider {

    CompaniesDto fetchData(String cin, Integer pageSize);
}
