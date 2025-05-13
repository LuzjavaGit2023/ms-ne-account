package pe.com.app.account.service;

import pe.com.app.account.common.config.DocumentType;
import pe.com.app.account.controller.request.AccountNewRequest;
import pe.com.app.account.controller.request.AccountUpdateRequest;
import pe.com.app.account.controller.request.DepositRequest;
import pe.com.app.account.controller.request.WithdrawalRequest;
import pe.com.app.account.controller.response.AccountResponse;
import pe.com.app.account.model.dto.AccountDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * <b>Interface</b>: ProductService <br/>
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
public interface AccountService {


    Mono<Void> newAccount(AccountNewRequest obj);

    Flux<AccountResponse> getAllAccountsByDocument(DocumentType documentType, String documentNumber);

    Mono<Void> updateAccount(String accountNumber, AccountUpdateRequest obj);


    Mono<Void> deleteAccount(String accountNumber);
    Mono<Void> depositAccount(String accountNumber, DepositRequest deposit);

    Mono<Void> withdrawalAccount(String accountNumber, WithdrawalRequest withdrawal);
}
