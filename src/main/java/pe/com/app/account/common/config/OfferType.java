package pe.com.app.account.common.config;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum OfferType {
    COMPRA("COMPRA"),
    VENTA("VENTA");
    private final String description;

    OfferType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    @JsonCreator
    public static OfferType fromString(String value) {
        return value != null ? OfferType.valueOf(value.toUpperCase()) : null;
    }

    @JsonValue
    public String toValue() {
        return this.name();
    }
}
