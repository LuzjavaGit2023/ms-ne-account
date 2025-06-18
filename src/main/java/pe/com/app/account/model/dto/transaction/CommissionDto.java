package pe.com.app.account.model.dto.transaction;

import java.io.Serializable;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pe.com.app.account.common.config.Currency;
import pe.com.app.account.common.config.TransactionOrigin;

/**
 * <b>class</b>: CommissionDto <br/>
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
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CommissionDto implements Serializable {
    private static final long serialVersionUID = 8811730754989102144L;
    private String serviceId;
    private Double amount;
    private Currency currency; // Moneda (USD, PEN, EUR, etc.)

    private TransactionOrigin origin;
    private String entityClient;
    private UUID feeId;
}
