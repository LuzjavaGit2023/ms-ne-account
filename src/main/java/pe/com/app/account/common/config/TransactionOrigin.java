package pe.com.app.account.common.config;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum TransactionOrigin {
    ACCOUNT("ACCOUNT"),
    CREDIT("CREDIT");

    private final String description;

    TransactionOrigin(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    @JsonCreator
    public static TransactionOrigin fromString(String value) {
        return value != null ? TransactionOrigin.valueOf(value) : null;
    }

    @JsonValue
    public String toValue() {
        return this.name();
    }
}
