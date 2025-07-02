package pe.com.app.account.common.config;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum PaymentModeP2p {
    YANKI("YANKI"),
    TRANSFER("TRANSFER");
    private final String description;

    PaymentModeP2p(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    @JsonCreator
    public static PaymentModeP2p fromString(String value) {
        return value != null ? PaymentModeP2p.valueOf(value.toUpperCase()) : null;
    }

    @JsonValue
    public String toValue() {
        return this.name();
    }
}
