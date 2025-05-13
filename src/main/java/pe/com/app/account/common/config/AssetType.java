package pe.com.app.account.common.config;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum AssetType {
    PersonalLoan("CreditoPersonal"),
    BusinessLoan("CreditoEmpresarial"),
    CreditCard("TarjetaCredito");

    private final String description;

    AssetType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    @JsonCreator
    public static AssetType fromString(String value) {
        return value != null ? AssetType.valueOf(value.toUpperCase()) : null;
    }

    @JsonValue
    public String toValue() {
        return this.name();
    }
}