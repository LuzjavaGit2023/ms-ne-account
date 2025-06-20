package pe.com.app.account.controller.request;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pe.com.app.account.common.config.Currency;
import pe.com.app.account.common.config.ProfileType;
import pe.com.app.account.model.dto.IndividualReferencedDto;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AccountNewRequest {
    private String productId;
    private String clientId;
    private ProfileType profile;
    private Currency currency; // Moneda (USD, PEN, EUR, etc.)
    private Integer maximumTransactionLimit;
    private Integer transactionDayEnable;
    private List<IndividualReferencedDto> headlines; //titulares
    private List<IndividualReferencedDto> signatories; //firmantes
}
