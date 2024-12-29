package com.incode.backendservice.base;


import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.common.ConsoleNotifier;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.incode.backendservice.BackendServiceApplication;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.TestPropertySourceUtils;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment =  SpringBootTest.WebEnvironment.RANDOM_PORT, classes = BackendServiceApplication.class)
@ContextConfiguration(initializers = AbstractIntegrationTest.EnvInitializer.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("embedded")
public abstract class AbstractIntegrationTest {

    private static WireMockServer wireMockServer;

    static {
        // Setup wiremock
        wireMockServer = new WireMockServer(
                options().port(WireMockConfiguration.wireMockConfig().dynamicHttpsPort().portNumber())
                        .notifier(new ConsoleNotifier(true)));

        wireMockServer.start();

        WireMockStub.stubWiremock(wireMockServer);
    }

    public static  class EnvInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {

            TestPropertySourceUtils.addInlinedPropertiesToEnvironment(applicationContext,
                    "backend.company.companyRegistryUrl=" + wireMockServer.baseUrl() +"/api");
        }
    }
}
