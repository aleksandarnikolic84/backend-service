package com.incode.backendservice.dataprovider.company.impl;

import com.incode.backendservice.dataprovider.company.api.CompanyDataProvider;
import com.incode.backendservice.dataprovider.company.handler.CompanyHandlerType;
import com.incode.backendservice.dataprovider.company.handler.api.CompanyDataHandler;
import com.incode.backendservice.dto.company.CompaniesDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;

@Slf4j
@Component
public class CompanyDataProviderImpl implements CompanyDataProvider {

    private final Map<CompanyHandlerType, CompanyDataHandler> handlerMap;
    private final List<String> orders;
    private CompanyDataHandler handler;

    @Autowired
    public CompanyDataProviderImpl(Set<CompanyDataHandler> providers,
                                   @Value("#{'${backend.company.order:}'.trim().split('\\s*,\\s*')}")
                                   List<String> orders) {
        this.orders = orders;
        this.handlerMap = providers.stream()
                .collect(Collectors.toMap(CompanyDataHandler::getType, Function.identity()));
    }

    @PostConstruct
    void populateProviders() {
        // Chain all providers based on the order
        handler = loadChain(orders, handlerMap);
    }

    /**
     * Load all handlers in chain
     * @param handlersOrder loaded order from configuration
     * @param handlers map of all handlers by type
     * @return first handler for execution
     */
    //TODO Cover this code with unit test
    private CompanyDataHandler loadChain(List<String> handlersOrder,
                                         Map<CompanyHandlerType, CompanyDataHandler> handlers) {

        log.info("Load Company data providers in following order:{}.", handlersOrder);

        CompanyDataHandler initialHandler = null;
        CompanyDataHandler previous = null;

        for (String order : handlersOrder) {
            CompanyDataHandler current = handlers.get(CompanyHandlerType.fromValue(order));
            if (current != null) {
                if (previous != null) {
                    previous.setNextDataProvider(current);
                } else {
                    initialHandler = current;
                }
                previous = current;
            } else {
                log.error("Data provider for type {} does not exist.", order);
                throw new IllegalStateException(String.format("Data provider for type %s does not exist.", order));
            }
        }

        // Safety check: if no providers were found
        if (initialHandler == null) {
            log.error("No valid Data provider instance.");
            throw new IllegalStateException("No valid Data provider instance.");
        }
        return initialHandler;
    }

    @Override
    public CompaniesDto fetchData(String cin, Integer pageSize) {
        log.info("Fetch companies from third party for cin:{} with pageSize:{}.", cin, pageSize);
        return handler.fetchData(cin, pageSize);
    }
}
