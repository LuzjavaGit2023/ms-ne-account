package pe.com.app.account.model.dto.credit;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pe.com.app.account.common.config.DeadLineToReturn;
import pe.com.app.account.common.config.PaymentFrequency;

/**
 * <b>class</b>: IndividualLoanDto <br/>
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
public class IndividualLoanDto implements Serializable {

    private static final long serialVersionUID = -3876300064368807306L;
    // Relacionado a control financiero
    private Double amount; // Monto otorgado al cliente
    private DeadLineToReturn termDeadLineToReturn; //plazo acordado en meses o anios
    private Integer termInMonths; // plazo acordado para devolver el prestamo
    private Double annualInterestRate; // tasa de Interes anual
    private Boolean fixedRate;
    private LocalDateTime disbursementDate; //fecha de desembolso
    private LocalDateTime startDate; // Fecha en que comienza el servicio
    private LocalDateTime endDate;  // Fecha estimada de finalizacion del servicio
    private Double outstandingBalance; // saldo pendiente Cuanto queda por pagar.
    private Integer totalInstallments; // numero de cuotas
    private Integer installmentsPaid; // coutas pagadas
    private PaymentFrequency paymentFrequency; // frecuencia de pago, cada cuanto tiempo debe realizar un pago
    private Double fixedMonthlyPayment; // Cuota mensual fija calculada
    private List<FeeDto> schedule;
}
