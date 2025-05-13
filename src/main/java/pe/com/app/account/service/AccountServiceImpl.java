package pe.com.app.account.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pe.com.app.account.common.config.AccountType;
import pe.com.app.account.common.config.ClientType;
import pe.com.app.account.common.config.DocumentType;
import pe.com.app.account.common.mapper.AccountMapper;
import pe.com.app.account.controller.request.AccountNewRequest;
import pe.com.app.account.controller.request.AccountUpdateRequest;
import pe.com.app.account.controller.request.DepositRequest;
import pe.com.app.account.controller.request.WithdrawalRequest;
import pe.com.app.account.controller.response.AccountResponse;
import pe.com.app.account.model.dto.client.ClientDto;
import pe.com.app.account.model.dto.product.ProductDto;
import pe.com.app.account.repository.AccountRepository;
import pe.com.app.account.webclient.BankingMovementClient;
import pe.com.app.account.webclient.ClientClient;
import pe.com.app.account.webclient.ProductClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * <b>Class</b>: ProductServiceImpl <br/>
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
@Service
@Slf4j
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository repository;
    private final BankingMovementClient bankingMovementClient;
    private final ClientClient clientClient;
    private final ProductClient productClient;

    @Override
    public Mono<Void> newAccount(AccountNewRequest obj) {
        log.info("newAccount : execute, request {}", obj);
        return productClient.getProduct(obj.getProductId())
                .flatMap(productDto -> clientClient.getClient(obj.getClientId())
                        .flatMap(clientDto ->
                                validateAccountsQuantity(productDto, clientDto, obj)
                                        .flatMap(aBoolean -> saveNewAccountValidated(obj))
                        )
                );
    }

    @Override
    public Flux<AccountResponse> getAllAccountsByDocument(DocumentType documentType, String documentNumber) {
        log.info("getAllAccountsByDocument : execute, documentType {}, documentNumber {} ", documentType, documentNumber);
        return null;
    }

    @Override
    public Mono<Void> updateAccount(String accountNumber, AccountUpdateRequest obj) {
        log.info("updateAccount : execute, accountNumber {}, request {}", accountNumber, obj);
        return null;
    }

    @Override
    public Mono<Void> deleteAccount(String accountNumber) {
        log.info("deleteAccount : execute, accountNumber {}", accountNumber);
        return null;
    }

    @Override
    public Mono<Void> depositAccount(String accountNumber, DepositRequest deposit) {
        log.info("depositAccount : execute, accountNumber {}, request {}", accountNumber, deposit);
        return null;
    }

    @Override
    public Mono<Void> withdrawalAccount(String accountNumber, WithdrawalRequest withdrawal) {
        log.info("withdrawalAccount : execute, accountNumber {}, request {}", accountNumber, withdrawal);
        return null;
    }

    private Mono<Void> validateAccountsQuantity(ProductDto productDto, ClientDto client, AccountNewRequest obj) {
        log.info("validateAccountsQuantity : start productDto : {}", productDto);
        log.info("validateAccountsQuantity : start client : {}", client);

        log.info("validateAccountsQuantity : ClientType : {}", client.getClientType());
        if(client.getClientType() == ClientType.NATURAL) {
            //1 maximo de cta de ahorro
            //1 maximo de cta corriente
            // varias cuentas a plazo fijo
            return repository.countByClientIdAndAccountType(client.getId(), AccountType.SAVINGS_ACCOUNT)
                    .flatMap(countSA -> {

                        log.info("validateAccountsQuantity : countByClientIdAndAccountType : {}", countSA);
                        if(countSA + isOneMoreSavingAccount(obj) > 1) {
                            return Mono.error(new IllegalStateException("El cliente(persona natural) ya tiene 1 cuenta de ahorro, no procede."));
                        }
                        return repository.countByClientIdAndAccountType(client.getId(), AccountType.CURRENT_ACCOUNT)
                                .flatMap(countCC -> {

                                    log.info("validateAccountsQuantity : countByClientIdAndAccountType : {}", countCC);
                                    if(countCC + isOneMoreCurrentAccount(obj) > 1) {
                                        return Mono.error(new IllegalStateException("El cliente(persona natural) ya tiene 1 cuenta corriente, no procede."));
                                    }

                                    log.info("validateAccountsQuantity : paso todas las validaciones P.NATURAL");
                                    return Mono.empty(); //no es necesario validar cuentas plazo fijo
                                });
                    });
        }
        else if (client.getClientType() == ClientType.BUSINESS) {
            //no puede tener de cta de ahorro
            //puede tener multiples cta corriente
            //no puede tener de cta a plazo fijo
            return Mono.defer(() -> {
                if((isOneMoreSavingAccount(obj) + isOneMoreFixedDeposit(obj)) > 0){ // trata de crear cta corriente o plazo fijo
                    return Mono.error(new IllegalStateException("El cliente(persona empresa) no puede tener cuenta de ahorro o de cuenta plazo fijo, no procede."));
                }

                //validar Titulares y Firmantes
                if(obj.getHeadlines() == null || obj.getHeadlines().isEmpty()) { //puede tener 1 o mas titulares.
                    return Mono.error(new IllegalStateException("El cliente(persona empresa) debe ingresar un titular como minimo, no procede."));
                }
                //Puede tner cero o mas firmantes autorizados, no es cesario validar cantidad
                //no es necesario validar cuentas plazo fijo

                log.info("validateAccountsQuantity : paso todas las validaciones P.BUSINESS");
                return Mono.empty();
            });
        }
        return Mono.error(new IllegalStateException("Tipo de cliente no identifiado, no procede."));
    }

    private int isOneMoreSavingAccount(AccountNewRequest obj) { //cta de ahorro
        return AccountType.SAVINGS_ACCOUNT == obj.getAccountType() ? 1 : 0 ;
    }

    private int isOneMoreCurrentAccount(AccountNewRequest obj) { //cta corriente
        return AccountType.CURRENT_ACCOUNT == obj.getAccountType() ? 1 : 0 ;
    }

    private int isOneMoreFixedDeposit(AccountNewRequest obj) { //cta a plazo fijo
        return AccountType.FIXED_DEPOSIT == obj.getAccountType() ? 1 : 0 ;
    }

    private Mono<Void> saveNewAccountValidated(AccountNewRequest obj) {
        return Mono.just(AccountMapper.buildEntityNew(obj))
                .flatMap(accountEntity -> repository.save(accountEntity))
                .then();
    }
}
