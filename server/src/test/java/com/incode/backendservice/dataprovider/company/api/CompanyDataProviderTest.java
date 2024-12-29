package com.incode.backendservice.dataprovider.company.api;

import com.incode.backendservice.dataprovider.company.handler.FreeCompanyDataHandler;
import com.incode.backendservice.dataprovider.company.handler.PremiumCompanyDataHandler;
import com.incode.backendservice.dataprovider.company.impl.CompanyDataProviderImpl;
import com.incode.backendservice.dataprovider.company.mapper.CompanyDataHandlerMapper;
import com.incode.backendservice.dto.company.CompaniesDto;
import com.incode.backendservice.dto.company.CompanyDataProviderData;
import com.incode.backendservice.dto.company.CompanyExecutionResultDto;
import com.incode.backendservice.dto.company.CompanyExecutionStatusDto;
import com.incode.backendservice.dto.company.remote.CompanyRemoteResponse;
import com.incode.backendservice.dto.company.remote.CompanyStatusRemote;
import com.incode.backendservice.external.api.company.api.CompanyClientRemote;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

@ExtendWith(SpringExtension.class)
@JsonTest
@ContextConfiguration(classes = {CompanyDataProviderImpl.class, CompanyDataHandlerMapper.class, FreeCompanyDataHandler.class, PremiumCompanyDataHandler.class})
@TestPropertySource(properties = {"backend.company.order=free, premium"})
class CompanyDataProviderTest {

    @Autowired
    private CompanyDataProvider sut;
    @MockBean(name = "freeCompanyRemoteClient")
    private CompanyClientRemote freeCompanyRemoteClient;
    @MockBean(name = "premiumCompanyRemoteClient")
    private CompanyClientRemote premiumCompanyRemoteClient;

    @ParameterizedTest
    @MethodSource("testFetchDataArguments")

    public void testFetchData(CompanyStatusRemote freeCompanyRemoteStatus, CompanyStatusRemote premiumCompanyRemoteStatus,
                              CompanyExecutionStatusDto expectedOverallExecutionStatus,
                              CompanyExecutionStatusDto expectedFirstExecutionStatus,
                              CompanyExecutionStatusDto expectedSecondExecutionStatus) {
        // Given
        String cin = "cin";
        Integer pageSize = 12;
        CompanyRemoteResponse freeCompanyRemoteResponse = new CompanyRemoteResponse();
        freeCompanyRemoteResponse.setStatus(freeCompanyRemoteStatus);
        Mockito.when(freeCompanyRemoteClient.findCompany(Mockito.eq(cin), Mockito.eq(pageSize))).thenReturn(freeCompanyRemoteResponse);
        CompanyRemoteResponse premiumCompanyRemoteResponse = new CompanyRemoteResponse();
        premiumCompanyRemoteResponse.setStatus(premiumCompanyRemoteStatus);
        Mockito.when(premiumCompanyRemoteClient.findCompany(Mockito.eq(cin), Mockito.eq(pageSize))).thenReturn(premiumCompanyRemoteResponse);
        //When
        CompaniesDto actualResult = sut.fetchData(cin, pageSize);
        //Then
        assertEquals(expectedOverallExecutionStatus, actualResult.getResultWrapper().getStatus());
        assertSame(2, actualResult.getExecutionResults().size());

        Assertions.assertThat(actualResult.getExecutionResults())
                .extracting(CompanyExecutionResultDto::getSource)
                .containsExactly(CompanyDataProviderData.FREE, CompanyDataProviderData.PREMIUM);

        Assertions.assertThat(actualResult.getExecutionResults())
                .extracting(CompanyExecutionResultDto::getStatus)
                .containsExactly(expectedFirstExecutionStatus, expectedSecondExecutionStatus);
    }


    private static Stream<Arguments> testFetchDataArguments() {
        return Stream.of(
                // Both execution failed -> overall failed
                Arguments.of(CompanyStatusRemote.FAILED, CompanyStatusRemote.FAILED, CompanyExecutionStatusDto.FAILED,
                        CompanyExecutionStatusDto.FAILED, CompanyExecutionStatusDto.FAILED),
                // First execution failed second success -> overall success
                Arguments.of(CompanyStatusRemote.FAILED, CompanyStatusRemote.SUCCESS, CompanyExecutionStatusDto.SUCCESS,
                        CompanyExecutionStatusDto.FAILED, CompanyExecutionStatusDto.SUCCESS),
                // Both execution no data -> overall no data
                Arguments.of(CompanyStatusRemote.NO_DATA, CompanyStatusRemote.NO_DATA, CompanyExecutionStatusDto.NO_DATA,
                        CompanyExecutionStatusDto.NO_DATA, CompanyExecutionStatusDto.NO_DATA),
                // First no data second success -> overall success
                Arguments.of(CompanyStatusRemote.NO_DATA, CompanyStatusRemote.SUCCESS, CompanyExecutionStatusDto.SUCCESS,
                        CompanyExecutionStatusDto.NO_DATA, CompanyExecutionStatusDto.SUCCESS)
        );
    }

    @Test
    public void testFetchData_firstExecutionSuccessNoSecond() {
        // Given
        CompanyRemoteResponse companyRemoteResponse = new CompanyRemoteResponse();
        companyRemoteResponse.setStatus(CompanyStatusRemote.SUCCESS);
        Mockito.when(freeCompanyRemoteClient.findCompany(Mockito.anyString(), Mockito.anyInt())).thenReturn(companyRemoteResponse);
        //When
        CompaniesDto actualResult = sut.fetchData("CIN", 12);
        //Then
        assertEquals(CompanyExecutionStatusDto.SUCCESS, actualResult.getResultWrapper().getStatus());
        assertSame(1, actualResult.getExecutionResults().size());

        Assertions.assertThat(actualResult.getExecutionResults())
                .extracting(CompanyExecutionResultDto::getStatus)
                .containsExactly(CompanyExecutionStatusDto.SUCCESS);
        Mockito.verify(premiumCompanyRemoteClient, Mockito.never()).findCompany(Mockito.anyString(), Mockito.anyInt());
    }
}