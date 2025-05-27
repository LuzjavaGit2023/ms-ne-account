package pe.com.app.account.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import pe.com.app.account.common.config.AccountStatus;
import pe.com.app.account.common.config.AccountType;
import pe.com.app.account.common.config.ClientType;
import pe.com.app.account.common.config.DocumentType;
import pe.com.app.account.common.mapper.AccountMapper;
import pe.com.app.account.common.util.Constant;
import pe.com.app.account.controller.request.AccountNewRequest;
import pe.com.app.account.controller.request.AccountUpdateRequest;
import pe.com.app.account.controller.request.DepositRequest;
import pe.com.app.account.controller.request.WithdrawalRequest;
import pe.com.app.account.controller.response.AccountNewResponse;
import pe.com.app.account.controller.response.AccountResponse;
import pe.com.app.account.model.dto.client.ClientDto;
import pe.com.app.account.model.dto.product.ProductDto;
import pe.com.app.account.model.persistence.AccountEntity;
import pe.com.app.account.repository.AccountRepository;
import pe.com.app.account.webclient.BankingMovementClient;
import pe.com.app.account.webclient.ClientClient;
import pe.com.app.account.webclient.ProductClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * <b>Class</b>: AccountServiceImpl <br/>
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
    public Mono<AccountNewResponse> newAccount(AccountNewRequest obj) {
        final var ob = AccountResponse.builder().build();
        log.info("newAccount : execute, request {}", obj);
        return productClient.getProduct(obj.getProductId())
                .flatMap(productDto -> validateCorrectProduct(productDto))
                .flatMap(productDto -> clientClient.getClient(obj.getClientId())
                        .flatMap(clientDto ->
                                validateAccountsQuantity(productDto, clientDto, obj)
                                        .flatMap(aBoolean -> validateFixedDepositAccount(productDto, clientDto, obj))
                                        .flatMap(aBoolean -> saveNewAccountValidated(obj, clientDto, productDto))
                        )
                );
    }

    @Override
    public Flux<AccountResponse> getAllAccountsByDocument(DocumentType documentType, String documentNumber) {
        log.info("getAllAccountsByDocument : execute, documentType {}, documentNumber {} "
                , documentType, documentNumber);
        return clientClient.getClientByDocument(documentType, documentNumber)
                .flatMapMany(clientDto -> repository.findByClientId(clientDto.getId())
                        .map(accountEntity -> AccountMapper.buildAccountResponse(accountEntity)));

    }

    @Override
    public Mono<AccountResponse> getAccountsByNumberAccount(String accountNumber) {
        log.info("getAccountsByNumberAccount : execute, accountNumber {}", accountNumber);
        return repository.findByAccountNumber(accountNumber)
                .switchIfEmpty(Mono.error(new IllegalStateException(Constant.ELEMENT_NOT_FOUND)))
                .flatMap(accountEntity -> {
                    if (AccountStatus.CERRADO.equals(accountEntity.getStatus())) {
                        return Mono.error(buildException(Constant.ELEMENT_NOT_ACTIVE));
                    }
                    return Mono.just(accountEntity);
                })
                .map(accountEntity -> AccountMapper.buildAccountResponse(accountEntity));
    }

    @Override
    public Mono<Void> updateAccount(String accountNumber, AccountUpdateRequest obj) {
        log.info("updateAccount : execute, accountNumber {}, request {}", accountNumber, obj);
        return repository.findByAccountNumber(accountNumber)
                .switchIfEmpty(Mono.error(new IllegalStateException(Constant.ELEMENT_NOT_FOUND)))
                .flatMap(accountEntity -> {
                    if (AccountStatus.CERRADO.equals(accountEntity.getStatus())) {
                        return Mono.error(buildException(Constant.ELEMENT_NOT_ACTIVE));
                    }
                    if (CollectionUtils.isEmpty(obj.getHeadlines())) {
                        return Mono.error(buildException(Constant.ELEMENT_ANY_HOLDER_PRESENT));
                    }
                    return Mono.just(accountEntity);
                })
                .flatMap(accountEntity -> repository.save(AccountMapper.buildEntityUpdate(accountEntity, obj)))
                .then();
    }

    @Override
    public Mono<Void> deleteAccount(String accountNumber) {
        log.info("deleteAccount : execute, accountNumber {}", accountNumber);
        return repository.findByAccountNumber(accountNumber)
                .switchIfEmpty(Mono.error(buildException(Constant.ELEMENT_NOT_FOUND)))
                .flatMap(accountEntity -> {
                    if (AccountStatus.CERRADO.equals(accountEntity.getStatus())) {
                        return Mono.error(buildException(Constant.ELEMENT_NOT_ACTIVE));
                    }
                    return repository.save(AccountMapper.buildEntityDelete(accountEntity));
                })
                .then();
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

    private Mono<Boolean> validateFixedDepositAccount(ProductDto product, ClientDto client, AccountNewRequest obj) {
        //configuracion de comision y limite de movimientos mensuales esta configurado en el producto
        //pero si es cuenta deposito fijo, se debe validar el dia habilitado para retiro o deposito del mes
        final var accountTypeSelected = AccountType.valueOf(product.getProductSubType());
        if (AccountType.FIXED_DEPOSIT.equals(accountTypeSelected)
                && (obj.getTransactionDayEnable() == null || obj.getTransactionDayEnable() <= 0
                || obj.getTransactionDayEnable() > 31 )) {
            return Mono.error(buildException(Constant.ANY_DAY_PRESENT_ACCOUNT));
        }
        return Mono.just(true);
    }

    private Mono<Boolean> validateAccountsQuantity(ProductDto product, ClientDto client, AccountNewRequest obj) {
        log.info("validateAccountsQuantity : start product : {}", product);
        log.info("validateAccountsQuantity : start client : {}", client);
        log.info("validateAccountsQuantity : ClientType : {}", client.getClientType());
        if (client.getClientType() == ClientType.NATURAL) {
            //1 maximo de cta de ahorro
            //1 maximo de cta corriente
            // varias cuentas a plazo fijo
            return repository.countByClientIdAndAccountType(client.getId(), AccountType.SAVINGS_ACCOUNT)
                    .flatMap(countSA -> {

                        log.info("validateAccountsQuantity : countByClientIdAndAccountType : {}", countSA);
                        if (countSA + isOneMoreSavingAccount(product) > 1) {
                            return Mono.error(buildException(Constant.PN_HAS_ONE_SAVINGS_ACCOUNT));
                        }
                        return repository.countByClientIdAndAccountType(client.getId(), AccountType.CURRENT_ACCOUNT)
                                .flatMap(countCC -> {

                                    log.info("validateAccountsQuantity : countByClientIdAndAccountType : {}", countCC);
                                    if (countCC + isOneMoreCurrentAccount(product) > 1) {
                                        return Mono.error(buildException(Constant.PN_HAS_ONE_CURRENT_ACCOUNT));
                                    }

                                    log.info("validateAccountsQuantity : paso todas las validaciones P.NATURAL");
                                    return Mono.just(true); //no es necesario validar cuentas plazo fijo
                                });
                    });
        }
        else if (client.getClientType() == ClientType.BUSINESS) {
            //no puede tener de cta de ahorro
            //puede tener multiples cta corriente
            //no puede tener de cta a plazo fijo
            return Mono.defer(() -> {
                if ((isOneMoreSavingAccount(product) + isOneMoreFixedDeposit(product)) > 0) {
                    // trata de crear cta corriente o plazo fijo
                    return Mono.error(buildException(Constant.PJ_NOT_VALID_ACCOUNT));
                }

                //validar Titulares y Firmantes
                if (obj.getHeadlines() == null || obj.getHeadlines().isEmpty()) { //puede tener 1 o mas titulares.
                    return Mono.error(buildException(
                            "El cliente(persona empresa) debe ingresar un titular como minimo, no procede."));
                }
                //Puede tner cero o mas firmantes autorizados, no es nesario validar cantidad
                //no es necesario validar cuentas plazo fijo

                log.info("validateAccountsQuantity : paso todas las validaciones P.BUSINESS");
                return Mono.just(true);
            });
        } else {
            return Mono.error(new IllegalStateException("Tipo de cliente no identifiado, no procede."));
        }
    }

    public static IllegalStateException buildException(String txt) {
        return new IllegalStateException(txt);
    }

    private Mono<AccountNewResponse> saveNewAccountValidated(AccountNewRequest obj,
                                                             ClientDto client,
                                                             ProductDto product) {
        log.info("saveNewAccountValidated, nueva cuenta {}", obj);
        return Mono.just(AccountMapper.buildEntityNew(obj, product))
                .flatMap(accountEntity -> assignNumberAccount(accountEntity, client, product))
                .flatMap(accountEntity -> repository.save(accountEntity))
                .map(accountEntity -> {
                    log.info("resultado del registro {}", accountEntity);
                    return AccountNewResponse.builder()
                            .id(accountEntity.getId())
                            .clientId(accountEntity.getClientId())
                            .accountNumber(accountEntity.getAccountNumber())
                            .productId(accountEntity.getProductId())
                            .accountType(accountEntity.getAccountType())
                            .build();
                });
    }

    private Mono<AccountEntity> assignNumberAccount(AccountEntity account, ClientDto client, ProductDto product) {
        final StringBuilder numberAccount = new StringBuilder("");
        switch (account.getAccountType()) {
            case SAVINGS_ACCOUNT : numberAccount.append("191-"); break;
            case CURRENT_ACCOUNT : numberAccount.append("192-"); break;
            case FIXED_DEPOSIT : numberAccount.append("193-"); break;
            default: numberAccount.append("190-");
        }
        switch (client.getClientType()) {
            case BUSINESS: numberAccount.append("20"); break;
            case NATURAL: numberAccount.append("10"); break;
            default: numberAccount.append("99");
        }

        switch (product.getProductType()) {
            case "Pasivo": numberAccount.append("1-"); break;
            case "Activo": numberAccount.append("2-"); break;
            default: numberAccount.append("0-");
        }
        final LocalDateTime horaActual = LocalDateTime .now();
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        final String random = horaActual.format(formatter);
        numberAccount.append(random);
        account.setAccountNumber(numberAccount.toString());
        return Mono.just(account);
    }

    private Mono<ProductDto> validateCorrectProduct(ProductDto productDto) {
        final boolean valid = Arrays.stream(AccountType.values())
                .anyMatch(r -> r.name().equalsIgnoreCase(productDto.getProductSubType()));
        log.info("Is product valid to account : {} on {}", valid, productDto.getProductSubType());
        if (valid) {
            return Mono.just(productDto);
        }
        return Mono.error(buildException("Producto seleccionado no es valido para una cuenta, no procede."));
    }


    private int isOneMoreSavingAccount(ProductDto product) { //cta de ahorro
        return AccountType.SAVINGS_ACCOUNT.getDescription().equals(product.getProductSubType()) ? 1 : 0 ;
    }

    private int isOneMoreCurrentAccount(ProductDto product) { //cta corriente
        return AccountType.CURRENT_ACCOUNT.getDescription().equals(product.getProductSubType()) ? 1 : 0 ;
    }

    private int isOneMoreFixedDeposit(ProductDto product) { //cta a plazo fijo
        return AccountType.FIXED_DEPOSIT.getDescription().equals(product.getProductSubType()) ? 1 : 0 ;
    }

}
