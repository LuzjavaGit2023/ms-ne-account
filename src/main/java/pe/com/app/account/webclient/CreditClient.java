package pe.com.app.account.webclient;

import pe.com.app.account.common.config.DocumentType;
import pe.com.app.account.model.dto.credit.CreditLoanDto;
import reactor.core.publisher.Flux;

public interface CreditClient {

    Flux<CreditLoanDto> getCreditsByDocument(DocumentType documentType, String documentNumber);
}
