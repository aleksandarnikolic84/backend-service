package com.incode.backendservice.external.api.company.config;

import com.incode.companyregistryservice.api.web.FreeCompaniesApi;
import com.incode.companyregistryservice.api.web.PremiumCompaniesApi;
import com.incode.companyregistryservice.api.web.invoker.ApiClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.ClientHttpRequestFactories;
import org.springframework.boot.web.client.ClientHttpRequestFactorySettings;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.RestClient;

import java.time.Duration;


/**
 * Configuration for Company Registry client over RestClient
 */
@Slf4j
@Configuration
public class CompanyRegistryClientConfiguration {


    @Value("${backend.company.companyRegistryUrl}")
    private String companyRegistryUrl;

    @Bean
    public ApiClient apiClient(final RestClient restClient) {
        ApiClient apiClient = new ApiClient(restClient);
        apiClient.setBasePath(companyRegistryUrl);
        return apiClient;
    }

    @Bean
    public FreeCompaniesApi freeCompaniesApi(final ApiClient apiClient) {
        return new FreeCompaniesApi(apiClient);
    }

    @Bean
    public PremiumCompaniesApi premiumCompaniesApi(final ApiClient apiClient) {
        return new PremiumCompaniesApi(apiClient);
    }

    @Bean
    public RestClient restClient() {
        return RestClient.builder()
                .requestFactory(customRequestFactory())
                .requestInterceptor(((request, body, execution) -> {
                    // There can be a lot of logs produces with this configuration. Use it for debug purpose
                    if (log.isDebugEnabled()) {
                        log.debug("[Rest client] Request: {} {}", request.getMethod(), request.getURI());
                        log.debug("[Rest client] Request Headers: {}", request.getHeaders());
                        log.debug("[Rest client] Request Body: {}", new String(body));
                    }
                    ClientHttpResponse response = execution.execute(request, body);
                    if (log.isDebugEnabled()) {
                        log.debug("[Rest client] Response Status: {}", response.getStatusCode());
                        log.debug("[Rest client] Response Headers: {}", response.getHeaders());
                    }
                    return response;
                }))
                .build();
    }

    ClientHttpRequestFactory customRequestFactory() {
        //TODO Timeout parameter should be externalized and configured over deployments
        ClientHttpRequestFactorySettings settings = ClientHttpRequestFactorySettings.DEFAULTS
                .withConnectTimeout(Duration.ofMillis(1000))
                .withReadTimeout(Duration.ofMillis(2000));
        return ClientHttpRequestFactories.get(settings);
    }
}
