package pe.com.app.account.model.dto.credit;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import pe.com.app.account.common.config.CreditType;
import pe.com.app.account.common.config.Currency;

/**
 * <b>class</b>: CreditLoanDto <br/>
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
@SuperBuilder
public class CreditLoanDto implements Serializable {
    private static final long serialVersionUID = -1092401934905256403L;
    private String id;
    private String productId;
    private String clientId;
    private CreditType creditType; // PERSONAL_LOAN BUSINESS_LOAN CREDIT_CARD
    private Currency currency; // Moneda (USD, PEN, EUR, etc.)

    private IndividualLoanDto individualLoan; // credito por persona o empresa
    private CreditCardDto creditCard; // tarjeta de crédito

}
