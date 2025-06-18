package pe.com.app.account.common.config;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum CardBrandType {
    VISA("VISA"),
    MASTERCARD("MASTERCARD"),
    AMEX("AMEX");

    private final String description;

    CardBrandType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    @JsonCreator
    public static CardBrandType fromString(String value) {
        return value != null ? CardBrandType.valueOf(value.toUpperCase()) : null;
    }

    @JsonValue
    public String toValue() {
        return this.name();
    }
}
