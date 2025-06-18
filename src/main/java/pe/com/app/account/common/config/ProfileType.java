package pe.com.app.account.common.config;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ProfileType {
    VIP("VIP"),
    PYME("PYME"),
    GENERAL("GENERAL");

    private final String description;

    ProfileType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    @JsonCreator
    public static ProfileType fromString(String value) {
        return value != null ? ProfileType.valueOf(value.toUpperCase()) : null;
    }

    @JsonValue
    public String toValue() {
        return this.name();
    }
}
