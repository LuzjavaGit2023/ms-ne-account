package pe.com.app.account.model.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import pe.com.app.account.common.config.AccountStatus;
import pe.com.app.account.common.config.AccountType;
import pe.com.app.account.common.config.Currency;
import pe.com.app.account.common.config.ProfileType;

@AllArgsConstructor
@NoArgsConstructor
@Data
@SuperBuilder
public class AccountDto implements Serializable {
    private static final long serialVersionUID = -5698870765128587736L;
    private String id;
    private String productId;
    private String clientId;
    private ProfileType profile;
    private String accountNumber;
    private AccountType accountType; // CuentaAhorro CuentaCorriente PlazoFijo
    private Currency currency; // Moneda (USD, PEN, EUR, etc.)
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private AccountStatus status; // ESTADO (ACTIVO, BLOQUEADO, CERRADO)

    // Relacionado a control financiero
    private Double balance; //valor actual en cuenta
    private Integer quantityCurrentTransactionCount; // Contador actual de transacciones
    private Integer transactionDayEnable; //dia especifico del mes para movimiento en cuenta plazo fijo

    private List<IndividualReferencedDto> headlines; //titulares
    private List<IndividualReferencedDto> signatories; //firmantes
}
