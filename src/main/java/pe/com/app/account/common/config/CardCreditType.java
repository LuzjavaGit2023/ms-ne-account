package pe.com.app.account.common.config;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum CardCreditType {
    CLASSIC("CLASSIC"),
    PLATINUM("PLATINUM"),
    GOLDEN("GOLDEN");

    private final String description;

    CardCreditType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    @JsonCreator
    public static CardCreditType fromString(String value) {
        return value != null ? CardCreditType.valueOf(value.toUpperCase()) : null;
    }

    @JsonValue
    public String toValue() {
        return this.name();
    }
}
