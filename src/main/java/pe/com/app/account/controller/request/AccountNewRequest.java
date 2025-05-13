package pe.com.app.account.controller.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pe.com.app.account.common.config.AccountType;
import pe.com.app.account.common.config.Currency;
import pe.com.app.account.model.dto.IndividualReferencedDto;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AccountNewRequest {
    private String productId;
    private String clientId;
    private AccountType accountType; // CuentaAhorro CuentaCorriente PlazoFijo
    private Currency currency; // Moneda (USD, PEN, EUR, etc.)
    private Integer transactionDayEnable;
    private List<IndividualReferencedDto> headlines; //titulares
    private List<IndividualReferencedDto> signatories; //firmantes
}
