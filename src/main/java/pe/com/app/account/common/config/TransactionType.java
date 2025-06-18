package pe.com.app.account.common.config;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum TransactionType {
    Withdrawal("Withdrawal"),
    Deposit("Deposit"),
    Payment("Payment"),
    Consumption("Consumption"),
    Commission("Commission");

    private final String description;

    TransactionType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    @JsonCreator
    public static TransactionType fromString(String value) {
        return value != null ? TransactionType.valueOf(value.toUpperCase()) : null;
    }

    @JsonValue
    public String toValue() {
        return this.name();
    }
}
