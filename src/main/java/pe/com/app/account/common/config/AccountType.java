package pe.com.app.account.common.config;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum AccountType {
    SAVINGS_ACCOUNT("SAVINGS_ACCOUNT"), //CuentaAhorro
    CURRENT_ACCOUNT("CURRENT_ACCOUNT"), //CuentaCorriente
    FIXED_DEPOSIT("FIXED_DEPOSIT"); //PlazoFijo

    private final String description;

    AccountType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    @JsonCreator
    public static AccountType fromString(String value) {
        return value != null ? AccountType.valueOf(value.toUpperCase()) : null;
    }

    @JsonValue
    public String toValue() {
        return this.name();
    }
}