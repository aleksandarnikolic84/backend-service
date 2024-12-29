package com.incode.backendservice.dataprovider.company.handler;

import com.incode.backendservice.dataprovider.company.handler.api.CompanyDataHandler;
import com.incode.backendservice.dataprovider.company.mapper.CompanyDataHandlerMapper;
import com.incode.backendservice.dto.company.remote.CompanyRemoteResponse;
import com.incode.backendservice.external.api.company.api.CompanyClientRemote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;


@Component
public class FreeCompanyDataHandler extends CompanyDataHandler {

    private final CompanyClientRemote companyClientRemote;

    @Autowired
    public FreeCompanyDataHandler(@Qualifier("freeCompanyRemoteClient") CompanyClientRemote companyClientRemote,
                                  CompanyDataHandlerMapper mapper) {
        super(mapper);
        this.companyClientRemote = companyClientRemote;
    }


    @Override
    protected CompanyRemoteResponse companyDataSupplier(String cin, Integer pageSize) {
        return companyClientRemote.findCompany(cin, pageSize);
    }

    @Override
    public CompanyHandlerType getType() {
        return CompanyHandlerType.FREE;
    }
}
