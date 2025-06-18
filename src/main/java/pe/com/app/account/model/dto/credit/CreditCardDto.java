package pe.com.app.account.model.dto.credit;

import java.io.Serializable;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pe.com.app.account.common.config.CardBrandType;
import pe.com.app.account.common.config.CardCreditType;
import pe.com.app.account.model.dto.IndividualReferencedDto;

/**
 * <b>class</b>: CreditCardDto <br/>
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
public class CreditCardDto implements Serializable {

    private static final long serialVersionUID = -2483218553439158923L;
    private String cardNumber; // Numero completo o enmascarado de la tarjeta
    //private String accountNumber; // Numero completo del numero de cuenta asociada tarjeta
    private IndividualReferencedDto cardHolder; // titular de la tarjeta
    private LocalDate expirationDate; // Fecha de vencimiento de la tarjeta
    private String securityCode; // CVV encriptado
    private String securityKey; // digital pasword
    private Double currentBalance; // Saldo actual usado
    private Double availableCredit;  // Saldo credito disponible
    private Double minimumPaymentDue; // Pago minimo requerido en el periodo actual
    private LocalDate paymentDueDate;  // Fecha de vencimiento del pago minimo
    private Double lastPaymentAmount;  // Monto del ultimo pago realizado
    private LocalDate lastPaymentDate;  // Fecha del ultimo pago
    private Integer billingDay;  // dia corte de facturacion
    private Boolean isContactlessEnabled;  // Si permite pagos sin contacto
    private CardBrandType cardBrand; // Visa, Mastercard, AMEX, etc.
    private CardCreditType cardCreditType; // CLASSIC, PLATINUM, GOLDEN.
    private String issuerBank; // Banco emisor de la tarjeta

}
