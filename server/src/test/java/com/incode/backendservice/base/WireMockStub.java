package com.incode.backendservice.base;

import com.github.tomakehurst.wiremock.WireMockServer;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class WireMockStub {


    /**
     * This can be resolved with full stub json with request and response signature
     * avoiding this stub class
     */
    public static void stubWiremock(WireMockServer wireMockServer) {

        //Add Free company api stub which return data
        wireMockServer
                .addStubMapping(
                        wireMockServer
                                .stubFor(get(urlEqualTo("/api/v1/free-third-party?query=DATA&page=0&size=4"))
                                        .willReturn(aResponse().withStatus(200)
                                                .withHeader("Content-Type", "application/json")
                                                .withBodyFile("free_company_response.json")
                                                .withTransformers("response-template"))));

        //Add Free company api stub which return no data
        wireMockServer
                .addStubMapping(
                        wireMockServer
                                .stubFor(get(urlEqualTo("/api/v1/free-third-party?query=FREE_NO_PREMIUM_YES&page=0&size=4"))
                                        .willReturn(aResponse().withStatus(200)
                                                .withHeader("Content-Type", "application/json")
                                                .withBodyFile("free_company_no_data.json")
                                                .withTransformers("response-template"))));

        wireMockServer
                .addStubMapping(
                        wireMockServer
                                .stubFor(get(urlEqualTo("/api/v1/free-third-party?query=NO_DATA&page=0&size=4"))
                                        .willReturn(aResponse().withStatus(200)
                                                .withHeader("Content-Type", "application/json")
                                                .withBodyFile("free_company_no_data.json")
                                                .withTransformers("response-template"))));

        //Add Free company api stub which return not available
        wireMockServer
                .addStubMapping(
                        wireMockServer
                                .stubFor(get(urlEqualTo("/api/v1/free-third-party?query=UNAVAILABLE&page=0&size=4"))
                                        .willReturn(aResponse().withStatus(503)
                                                .withHeader("Content-Type", "application/json")
                                                .withBodyFile("free_company_unavailable.json")
                                                .withTransformers("response-template"))));


        //Add Premium company api stub which return data
        wireMockServer
                .addStubMapping(
                        wireMockServer
                                .stubFor(get(urlEqualTo("/api/v1/premium-third-party?query=FREE_NO_PREMIUM_YES&page=0&size=4"))
                                        .willReturn(aResponse().withStatus(200)
                                                .withHeader("Content-Type", "application/json")
                                                .withBodyFile("premium_company_response.json")
                                                .withTransformers("response-template"))));

        //Add Premium company api stub which return no data
        wireMockServer
                .addStubMapping(
                        wireMockServer
                                .stubFor(get(urlEqualTo("/api/v1/premium-third-party?query=NO_DATA&page=0&size=4"))
                                        .willReturn(aResponse().withStatus(200)
                                                .withHeader("Content-Type", "application/json")
                                                .withBodyFile("premium_company_no_data.json")
                                                .withTransformers("response-template"))));

        //Add Premium company api stub which return not available
        wireMockServer
                .addStubMapping(
                        wireMockServer
                                .stubFor(get(urlEqualTo("/api/v1/premium-third-party?query=UNAVAILABLE&page=0&size=4"))
                                        .willReturn(aResponse().withStatus(503)
                                                .withHeader("Content-Type", "application/json")
                                                .withBodyFile("premium_company_unavailable.json")
                                                .withTransformers("response-template"))));
    }
}
