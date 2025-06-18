package pe.com.app.account.common.config;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum DeadLineToReturn {
    MONTHS_6("MONTHS_6"),
    MONTHS_12("MONTHS_12"),
    MONTHS_18("MONTHS_18"),
    YEARS_2("YEARS_2"),
    YEARS_3("YEARS_3"),
    YEARS_4("YEARS_4"),
    YEARS_5("YEARS_5"),
    YEARS_6("YEARS_6"),
    YEARS_7("YEARS_7"),
    YEARS_8("YEARS_8");

    private final String description;

    DeadLineToReturn(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    @JsonCreator
    public static DeadLineToReturn fromString(String value) {
        return value != null ? DeadLineToReturn.valueOf(value.toUpperCase()) : null;
    }

    @JsonValue
    public String toValue() {
        return this.name();
    }
}
