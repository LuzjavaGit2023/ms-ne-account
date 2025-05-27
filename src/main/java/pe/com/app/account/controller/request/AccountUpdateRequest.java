package pe.com.app.account.controller.request;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pe.com.app.account.model.dto.IndividualReferencedDto;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AccountUpdateRequest {
    private Integer transactionDayEnable; //dia especifico del mes para movimiento en cuenta plazo fijo

    private List<IndividualReferencedDto> headlines; //titulares, minimo 1
    private List<IndividualReferencedDto> signatories; //firmantes
}
