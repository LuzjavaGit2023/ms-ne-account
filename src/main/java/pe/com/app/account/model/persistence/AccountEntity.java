package pe.com.app.account.model.persistence;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import pe.com.app.account.common.config.AccountStatus;
import pe.com.app.account.common.config.AccountType;
import pe.com.app.account.common.config.Currency;
import pe.com.app.account.common.config.ProfileType;
import pe.com.app.account.model.dto.IndividualReferencedDto;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Document(collection = "accounts")
public class AccountEntity implements Serializable {
    private static final long serialVersionUID = 7346145359381795413L;
    @Id
    private String id;
    private String productId;
    private String clientId;
    private ProfileType profile;
    private String accountNumber;
    private AccountType accountType; // CuentaAhorro CuentaCorriente PlazoFijo
    private Currency currency; // Moneda (USD, PEN, EUR, etc.)
    private LocalDateTime lastTransactionDate;
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
