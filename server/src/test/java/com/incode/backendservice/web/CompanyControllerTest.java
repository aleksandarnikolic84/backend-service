package com.incode.backendservice.web;

import com.incode.backendservice.api.web.response.CompaniesResponse;
import com.incode.backendservice.api.web.response.CompanyResultsWrapperResponse;
import com.incode.backendservice.api.web.response.ErrorResponse;
import com.incode.backendservice.base.AbstractControllerTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.apache.commons.lang3.RandomStringUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

public class CompanyControllerTest extends AbstractControllerTest {


    @Test
    public void testBackendService_freeReturnResult() {
        // Given
        String query = "DATA";
        String verification = RandomStringUtils.random(5);
        // When
        CompaniesResponse actualResult = RestAssured.with().accept(ContentType.JSON).contentType(ContentType.JSON)
                .queryParam("query", query)
                .queryParam("verificationId", verification)
                .queryParam("upToSize", 2)
                .when().get("/backend-service")
                .then().log().body().assertThat().statusCode(HttpStatus.OK.value()).extract().as(CompaniesResponse.class);
        // Then
        Assertions.assertThat(actualResult.getQuery()).isEqualTo(query);
        Assertions.assertThat(actualResult.getVerificationId()).isEqualTo(verification);
        CompanyResultsWrapperResponse actualResultWrapper = actualResult.getResults();
        Assertions.assertThat(actualResultWrapper.getPossibleMatches()).isEqualTo(3);
        Assertions.assertThat(actualResultWrapper.getMatch()).isNotNull();
        Assertions.assertThat(actualResultWrapper.getOtherResults()).isEmpty();
    }

    @Test
    public void testBackendService_freeNoPremiumYes() {
        // Given
        String query = "FREE_NO_PREMIUM_YES";
        String verification = RandomStringUtils.random(5);
        // When
        CompaniesResponse actualResult = RestAssured.with().accept(ContentType.JSON).contentType(ContentType.JSON)
                .queryParam("query", query)
                .queryParam("verificationId", verification)
                .queryParam("upToSize", 2)
                .when().get("/backend-service")
                .then().log().body().assertThat().statusCode(HttpStatus.OK.value()).extract().as(CompaniesResponse.class);
        // Then
        Assertions.assertThat(actualResult.getQuery()).isEqualTo(query);
        Assertions.assertThat(actualResult.getVerificationId()).isEqualTo(verification);
        CompanyResultsWrapperResponse actualResultWrapper = actualResult.getResults();
        Assertions.assertThat(actualResultWrapper.getPossibleMatches()).isEqualTo(1);
        Assertions.assertThat(actualResultWrapper.getMatch()).isNotNull();
        Assertions.assertThat(actualResultWrapper.getOtherResults()).isEmpty();
    }

    @Test
    public void testBackendService_noData() {
        // Given
        String query = "NO_DATA";
        String verification = RandomStringUtils.random(5);
        // When
        CompaniesResponse actualResult = RestAssured.with().accept(ContentType.JSON).contentType(ContentType.JSON)
                .queryParam("query", query)
                .queryParam("verificationId", verification)
                .queryParam("upToSize", 2)
                .when().get("/backend-service")
                .then().log().body().assertThat().statusCode(HttpStatus.OK.value()).extract().as(CompaniesResponse.class);
        // Then
        Assertions.assertThat(actualResult.getQuery()).isEqualTo(query);
        Assertions.assertThat(actualResult.getVerificationId()).isEqualTo(verification);
        CompanyResultsWrapperResponse actualResultWrapper = actualResult.getResults();
        Assertions.assertThat(actualResultWrapper.getPossibleMatches()).isEqualTo(0);
        Assertions.assertThat(actualResultWrapper.getMatch()).isNull();
        Assertions.assertThat(actualResultWrapper.getOtherResults()).isEmpty();
    }


    @Test
    public void testBackendService_unavailable() {
        // Given
        String query = "UNAVAILABLE";
        String verification = RandomStringUtils.random(5);
        // When
        ErrorResponse actualResult = RestAssured.with().accept(ContentType.JSON).contentType(ContentType.JSON)
                .queryParam("query", query)
                .queryParam("verificationId", verification)
                .queryParam("upToSize", 2)
                .when().get("/backend-service")
                .then().log().body().assertThat().statusCode(HttpStatus.SERVICE_UNAVAILABLE.value()).extract().as(ErrorResponse.class);
        // Then
        Assertions.assertThat(actualResult.getError()).isNotNull();
        Assertions.assertThat(actualResult.getErrorCode()).isEqualTo(HttpStatus.SERVICE_UNAVAILABLE.name());
        Assertions.assertThat(actualResult.getTimestamp()).isNotNull();
    }
}