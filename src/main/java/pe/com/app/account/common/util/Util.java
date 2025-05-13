package pe.com.app.account.common.util;

import lombok.experimental.UtilityClass;

/**
 * <b>Class</b>: Util <br/>
 * <b>Copyright</b>: 2025 Tu Banco - Celula <br/>
 * .
 *
 * @author 2025 Tu Banco - Peru <br/>
 * <u>Service Provider</u>: Tu Banco <br/>
 * <u>Changes:</u><br/>
 * <ul>
 * <li>
 * May 10, 2025 Creación de Clase.
 * </li>
 * </ul>
 */
@UtilityClass
public class Util {

    /**
     * This method is used to validate value.
     */
    public static String sanitized(String value) {
        return value != null ? value.replace("\n", "").replace("\r", "") : null;
    }
}
