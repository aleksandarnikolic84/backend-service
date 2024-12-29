package com.incode.backendservice.dto.company.remote;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record SingleCompanyRemote(String cin, String address, String companyName,
                                  Boolean isActive, LocalDate registrationDate){}
