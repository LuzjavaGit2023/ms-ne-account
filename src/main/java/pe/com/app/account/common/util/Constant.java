package pe.com.app.account.common.util;

public class Constant {
    public static final String ERROR_CODE = "Error on Service";
    public static final String ELEMENT_EXIST_BY_DOCUMENT = "Cliente ya existe con ese tipo y numero de documento";
    public static final String ELEMENT_NOT_FOUND = "Cuenta no encontrada con el id indicado";
    public static final String ELEMENT_NOT_FOUND_BY_NUMBER_ACCOUNT = "Cuenta no encontrada con el numero de cuenta";
    public static final String ELEMENT_NOT_ACTIVE = "Cuenta actualmente esta Cerrada";

    public static final String ELEMENT_ANY_HOLDER_PRESENT = "Debe haber como minimo un titular declarado en la cuenta.";

    public static final String ANY_DAY_PRESENT_ACCOUNT = "En plazo fijo, debe indicar dia para retiro o deposito";
    public static final String PN_HAS_ONE_SAVINGS_ACCOUNT = "El cliente(persona natural) ya tiene 1 cuenta de ahorro.";
    public static final String PN_HAS_ONE_CURRENT_ACCOUNT = "En plazo fijo, debe indicar dia para retiro o deposito";
    public static final String PJ_NOT_VALID_ACCOUNT = "Empresa no puede tener Cta de ahorro o de cuenta P.Fijo.";

    public static final String PROFILE_NOT_ENABLE = "El perfil elegido, debe tener una Tarjeta de credito con el Banco";

    public static final String NOT_SAME_CURRENCY = "No se puede hacer transferencia, son de diferente Moneda";
}
