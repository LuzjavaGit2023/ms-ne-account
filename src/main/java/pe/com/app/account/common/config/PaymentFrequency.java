package pe.com.app.account.common.config;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum PaymentFrequency {
    MONTHLY("MONTHLY")/*,
    BIWEEKLY("BIWEEKLY"),
    WEEKLY("WEEKLY"),
    QUARTERLY("QUARTERLY"),
    ANNUAL("ANNUAL"),
    ONE_TIME("ONE_TIME")*/;

    private final String description;

    PaymentFrequency(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    @JsonCreator
    public static PaymentFrequency fromString(String value) {
        return value != null ? PaymentFrequency.valueOf(value.toUpperCase()) : null;
    }

    @JsonValue
    public String toValue() {
        return this.name();
    }
}
