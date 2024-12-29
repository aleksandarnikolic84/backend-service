package com.incode.backendservice.dto.company;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record CompanyDto(String cin, String address, String companyName,
                         Boolean isActive, LocalDate registrationDate) {

}
