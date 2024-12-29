package com.incode.backendservice.base;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.web.server.LocalServerPort;

public abstract class AbstractControllerTest extends AbstractIntegrationTest {
    @LocalServerPort
    private int port;

    @BeforeAll
    public void testUp(){
        RestAssured.port = port;
        RestAssured.basePath = "/api/v1";
    }

}
