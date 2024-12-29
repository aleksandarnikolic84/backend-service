package com.incode.backendservice.service.api;

import com.incode.backendservice.dataprovider.company.api.CompanyDataProvider;
import com.incode.backendservice.dto.company.CompaniesDto;
import com.incode.backendservice.dto.company.CompanyExecutionStatusDto;
import com.incode.backendservice.exception.CustomException;
import com.incode.backendservice.exception.CustomExceptionKey;
import com.incode.backendservice.inventory.CompanyDtoInventory;
import com.incode.backendservice.service.impl.CompanyServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {CompanyServiceImpl.class})
class CompanyServiceTest {

    @Autowired
    private CompanyService sut;

    @MockBean
    private CompanyDataProvider dataProvider;

    @Test
    void testFindByCin() {
        // Given
        int activeCompanies = 5;
        int inactiveCompanies = 7;
        CompaniesDto companiesDto = CompanyDtoInventory
                .createCompaniesDto(activeCompanies, inactiveCompanies, CompanyExecutionStatusDto.SUCCESS);
        Mockito.when(dataProvider.fetchData(Mockito.anyString(), Mockito.anyInt())).thenReturn(companiesDto);
        //When
        CompaniesDto actualResult = sut.findByCin("cin", null);
        //Then
        Assertions.assertThat(actualResult.getResultWrapper().getCompanies()).hasSize(activeCompanies);
    }

    @Test
    void testFindByCinUpToSize() {
        // Given
        int activeCompanies = 5;
        int inactiveCompanies = 7;
        int upToSize = 2;
        CompaniesDto companiesDto = CompanyDtoInventory
                .createCompaniesDto(activeCompanies, inactiveCompanies, CompanyExecutionStatusDto.SUCCESS);
        Mockito.when(dataProvider.fetchData(Mockito.anyString(), Mockito.anyInt())).thenReturn(companiesDto);
        //When
        CompaniesDto actualResult = sut.findByCin("cin", upToSize);
        //Then
        Assertions.assertThat(actualResult.getResultWrapper().getCompanies()).hasSize(upToSize);
    }

    @Test
    void testFindByCinUpToSizeZero() {
        // Given
        int activeCompanies = 5;
        int inactiveCompanies = 7;
        int upToSize = 0;
        CompaniesDto companiesDto = CompanyDtoInventory
                .createCompaniesDto(activeCompanies, inactiveCompanies, CompanyExecutionStatusDto.SUCCESS);
        Mockito.when(dataProvider.fetchData(Mockito.anyString(), Mockito.anyInt())).thenReturn(companiesDto);
        //When
        CompaniesDto actualResult = sut.findByCin("cin", upToSize);
        //Then
        Assertions.assertThat(actualResult.getResultWrapper().getCompanies()).hasSize(activeCompanies);
    }

    @Test
    void testFindByCin_Failed() {
        // Given
        int activeCompanies = 0;
        int inactiveCompanies = 0;
        CompaniesDto companiesDto = CompanyDtoInventory
                .createCompaniesDto(activeCompanies, inactiveCompanies, CompanyExecutionStatusDto.FAILED);
        Mockito.when(dataProvider.fetchData(Mockito.anyString(), Mockito.anyInt())).thenReturn(companiesDto);
        //When
        //Then
        CustomException actualResult = Assertions.catchThrowableOfType( CustomException.class,()
                -> sut.findByCin("cin", null));
        Assertions.assertThat(actualResult.getCustomExceptionKey()).isEqualTo(CustomExceptionKey.SERVICE_UNAVAILABLE);
    }
}