package pe.com.app.account.common.config;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum CreditType {
    PERSONAL_LOAN("PERSONAL_LOAN"), // credito por persona
    BUSINESS_LOAN("BUSINESS_LOAN"), // credito por empresa
    CREDIT_CARD("CREDIT_CARD"); // tarjeta de credito
    private final String description;

    CreditType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    @JsonCreator
    public static CreditType fromString(String value) {
        return value != null ? CreditType.valueOf(value.toUpperCase()) : null;
    }

    @JsonValue
    public String toValue() {
        return this.name();
    }
}
