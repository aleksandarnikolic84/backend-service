package com.incode.backendservice.dataprovider.company.handler;

import lombok.Getter;

import java.util.Objects;

@Getter
public enum CompanyHandlerType {

    FREE("free"), PREMIUM("premium");

    private final String name;

    CompanyHandlerType(String name) {
        this.name = name;
    }

    public static CompanyHandlerType fromValue(String value) {
        for (CompanyHandlerType type : CompanyHandlerType.values()) {
            if (Objects.equals(value, type.getName())) {
                return type;
            }
        }
        throw new IllegalArgumentException(String.format("Unexpected value '%s'", value));
    }
}
