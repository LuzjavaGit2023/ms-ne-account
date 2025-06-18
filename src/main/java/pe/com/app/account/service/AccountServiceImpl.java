package pe.com.app.account.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import pe.com.app.account.common.config.*;
import pe.com.app.account.common.mapper.AccountMapper;
import pe.com.app.account.common.util.Constant;
import pe.com.app.account.controller.request.AccountNewRequest;
import pe.com.app.account.controller.request.AccountUpdateRequest;
import pe.com.app.account.controller.request.DepositRequest;
import pe.com.app.account.controller.request.TransferRequest;
import pe.com.app.account.controller.request.WithdrawalRequest;
import pe.com.app.account.controller.response.AccountNewResponse;
import pe.com.app.account.controller.response.AccountResponse;
import pe.com.app.account.model.dto.ValidateDto;
import pe.com.app.account.model.dto.client.ClientDto;
import pe.com.app.account.model.dto.product.ProductDto;
import pe.com.app.account.model.dto.transaction.CommissionDto;
import pe.com.app.account.model.dto.transaction.TransactionResponseDto;
import pe.com.app.account.model.persistence.AccountEntity;
import pe.com.app.account.repository.AccountRepository;
import pe.com.app.account.webclient.ClientClient;
import pe.com.app.account.webclient.CreditClient;
import pe.com.app.account.webclient.ProductClient;
import pe.com.app.account.webclient.TransactionClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

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
    private final ClientClient clientClient;
    private final ProductClient productClient;
    private final TransactionClient transactionClient;
    private final CreditClient creditClient;

    @Override
    public Mono<AccountNewResponse> newAccount(AccountNewRequest obj) {
        log.info("newAccount :::::::::::::::::::: execute, request {}", obj);
        return productClient.getProduct(obj.getProductId())
                .flatMap(this::validateCorrectProduct)
                .flatMap(productDto -> clientClient.getClient(obj.getClientId())
                        .flatMap(clientDto ->
                                validateAccountsQuantity(productDto, clientDto, obj)
                                        .flatMap(aBoolean -> validateEspecialProfile(obj, clientDto))
                                        .flatMap(aBoolean -> validateFixedDepositAccount(productDto, obj))
                                        .flatMap(aBoolean -> saveNewAccountValidated(obj, clientDto, productDto))
                        )
                );
    }

    @Override
    public Flux<AccountResponse> getAllAccountsByDocument(DocumentType documentType, String documentNumber) {
        log.info("getAllAccountsByDocument :::::::::::::::::::: execute, documentType {}, documentNumber {} "
                , documentType, documentNumber);
        return clientClient.getClientByDocument(documentType, documentNumber)
                .flatMapMany(clientDto -> repository.findByClientId(clientDto.getId())
                        .map(AccountMapper::buildAccountResponse));

    }

    @Override
    public Mono<AccountResponse> getAccountsByNumberAccount(String accountNumber) {
        log.info("getAccountsByNumberAccount :::::::::::::::::::: execute, accountNumber {}", accountNumber);
        return repository.findByAccountNumber(accountNumber)
                .switchIfEmpty(Mono.error(new IllegalStateException(Constant.ELEMENT_NOT_FOUND)))
                .flatMap(accountEntity -> {
                    if (AccountStatus.CERRADO.equals(accountEntity.getStatus())) {
                        return Mono.error(buildException(Constant.ELEMENT_NOT_ACTIVE));
                    }
                    return Mono.just(accountEntity);
                })
                .map(AccountMapper::buildAccountResponse);
    }

    @Override
    public Mono<AccountResponse> getAccountById(String accountId) {
        log.info("getAccountById :::::::::::::::::::: execute, accountId {}", accountId);
        return repository.findById(accountId)
                .switchIfEmpty(Mono.error(new IllegalStateException(Constant.ELEMENT_NOT_FOUND)))
                .map(AccountMapper::buildAccountResponse);
    }

    @Override
    public Mono<Void> updateAccount(String accountNumber, AccountUpdateRequest obj) {
        log.info("updateAccount :::::::::::::::::::: execute, accountNumber {}, request {}", accountNumber, obj);
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
        log.info("deleteAccount :::::::::::::::::::: execute, accountNumber {}", accountNumber);
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
        log.info("depositAccount :::::::::::::::::::: execute, accountNumber {}, request {}", accountNumber, deposit);
        return repository.findByAccountNumber(accountNumber)
                .switchIfEmpty(Mono.error(new IllegalStateException(Constant.ELEMENT_NOT_FOUND_BY_NUMBER_ACCOUNT)))
                .flatMap(accountEntity -> productClient.getProduct(accountEntity.getProductId())
                        .flatMap(this::validateCorrectProduct)
                        .flatMap(productDto -> {
                            log.info("AccountType : {}", accountEntity.getAccountType());
                            if (AccountStatus.CERRADO.equals(accountEntity.getStatus())) {
                                return Mono.error(new IllegalStateException(Constant.ELEMENT_NOT_ACTIVE));
                            }
                            if (deposit.getCurrency() == null) {
                                deposit.setCurrency(accountEntity.getCurrency());
                            }
                            //registrar transaccion
                            return validateAvailableDeposit(accountEntity, deposit, productDto)
                                    .flatMap(validation ->
                                            executeDeposit(accountEntity, deposit, productDto, validation));
                        })
                )
        .then();
    }

    @Override
    public Mono<Void> withdrawalAccount(String accountNumber, WithdrawalRequest withdrawal) {
        log.info("withdrawalAccount :::::::::::::::::::: execute, accountNumber {}, request {}"
                , accountNumber, withdrawal);
        return repository.findByAccountNumber(accountNumber)
                .switchIfEmpty(Mono.error(new IllegalStateException(Constant.ELEMENT_NOT_FOUND_BY_NUMBER_ACCOUNT)))
                .flatMap(accountEntity -> productClient.getProduct(accountEntity.getProductId())
                        .flatMap(this::validateCorrectProduct)
                        .flatMap(productDto -> {
                            log.info("AccountType : {}", accountEntity.getAccountType());
                            if (AccountStatus.CERRADO.equals(accountEntity.getStatus())) {
                                return Mono.error(new IllegalStateException(Constant.ELEMENT_NOT_ACTIVE));
                            }
                            if (withdrawal.getCurrency() == null) {
                                withdrawal.setCurrency(accountEntity.getCurrency());
                            }
                            //registrar transaccion
                            return validateAvailableWithdrawal(accountEntity, withdrawal, productDto)
                                    .flatMap(validation ->
                                            executeWithdrawal(accountEntity, withdrawal, productDto, validation));
                        })
                )
                .then();
    }

    @Override
    public Mono<Void> transferAccount(String accountNumber, TransferRequest transfer) {

        final var queryAcc1 = getAccount(accountNumber);
        final var queryAcc2 = getAccount(transfer.getDestinationAccount());

        return Mono.zip(queryAcc1, queryAcc2)
                .flatMap(tuple -> validateTransferType(tuple))
                .flatMap(tuple -> {
                    log.info("Cuentas identificadas");
                    final var account1 = tuple.getT1();
                    final var account2 = tuple.getT2();

                    final var queryProd1 = getProduct(account1.getProductId());
                    final var queryProd2 = getProduct(account2.getProductId());

                    return Mono.zip(queryProd1, queryProd2)
                            .flatMap(tupleProd -> {
                                log.info("Productos identificados");
                                final var prod1 = tupleProd.getT1();
                                final var prod2 = tupleProd.getT2();

                                final var withdrawal = WithdrawalRequest.builder()
                                        .amount(transfer.getAmount())
                                        .currency(account1.getCurrency())
                                        .entityClient("Transf to " + transfer.getDestinationAccount())
                                        .build();

                                final var deposit = DepositRequest.builder()
                                        .amount(transfer.getAmount())
                                        .currency(account2.getCurrency())
                                        .entityClient("Transf from " + accountNumber)
                                        .build();

                                final var valid1 = validateAvailableWithdrawal(account1, withdrawal, prod1);
                                final var valid2 = validateAvailableDeposit(account2, deposit, prod2);

                                return Mono.zip(valid1, valid2)
                                        .flatMap(tupleValidate -> {
                                            log.info("Validaciones por cada cuenta realizadas");
                                            final var validated1 = tupleValidate.getT1();
                                            final var validated2 = tupleValidate.getT2();

                                            return executeWithdrawal(account1, withdrawal, prod1, validated1)
                                                    .flatMap(ac -> executeDeposit(account2, deposit, prod2, validated2))
                                                    .doOnNext(ac -> log.info("Transferencia Terminada"));

                                        });
                            });
                }).then();
    }

    private Mono<Tuple2<AccountEntity, AccountEntity>> validateTransferType(Tuple2<AccountEntity,
            AccountEntity> tuple) {
        final AccountEntity accountOrigin = tuple.getT1();
        final AccountEntity accountDestination = tuple.getT2();
        final var currencyOrigin = accountOrigin.getCurrency();
        final var currencyDestination = accountDestination.getCurrency();
        if (accountOrigin.getAccountNumber().equals(accountDestination.getAccountNumber())) {
            log.info("La transferencia no puede ser a la misma cuenta");
            return Mono.error(new IllegalStateException("La transferencia no puede ser a la misma cuenta"));

        }
        if (accountOrigin.getClientId().equals(accountDestination.getClientId())) {
            log.info("Transferencia entre cuentas del mismo Cliente del mismo banco");
            log.info("Cuentas son del mismo cliente id : {}", accountOrigin.getClientId());
        } else {
            log.info("Transferencia entre cuentas a Tercero del mismo Banco");
            log.info("Cuenta Origen, cliente id : {}", accountOrigin.getClientId());
            log.info("Cuenta Destino, cliente id : {}", accountDestination.getClientId());
        }
        if (!currencyOrigin.equals(currencyDestination)) {
            log.info("No se puede hacer transferencia entre cuentas que son de diferente Moneda");
            log.info("Moneda origen({}), Moneda Destino({})", currencyOrigin, currencyDestination);

            return Mono.error(new IllegalStateException(Constant.NOT_SAME_CURRENCY));
        }
        return Mono.just(tuple);
    }

    private Mono<ProductDto> getProduct(String idProduct) {
        log.info("buscando producto, idProduct : {}", idProduct);
        return productClient.getProduct(idProduct)
                //.switchIfEmpty(Mono.error(new IllegalStateException("No hay producto")))
                .flatMap(productDto -> {
                    log.info("Producto encontrado : " + productDto.getLabel());
                    return validateCorrectProduct(productDto);
                });
    }

    private Mono<AccountEntity> getAccount(String accountNumber) {
        return repository.findByAccountNumber(accountNumber)
                .switchIfEmpty(Mono.error(new IllegalStateException(Constant.ELEMENT_NOT_FOUND_BY_NUMBER_ACCOUNT
                        + " " + accountNumber)))
                .flatMap(accountEntity -> {
                    log.info("AccountType : {}", accountEntity.getAccountType());
                    if (AccountStatus.CERRADO.equals(accountEntity.getStatus())) {
                        return Mono.error(new IllegalStateException(Constant.ELEMENT_NOT_ACTIVE + " " + accountNumber));
                    }
                    return Mono.just(accountEntity);
                });
    }

    private Mono<Boolean> validateEspecialProfile(AccountNewRequest obj, ClientDto client) {
        log.info("Validando si el PERFIL( VIP o PYME) asignado, necesita evaluar TARJETA DE CREDITO");

        //Perfil VIP o PIME, se validar que el cliente tenga una tarjeta de credito
        if ((client.getClientType().equals(ClientType.NATURAL)
                && obj.getProfile() != null && obj.getProfile().equals(ProfileType.VIP))
                || (client.getClientType().equals(ClientType.BUSINESS)
                && obj.getProfile() != null && obj.getProfile().equals(ProfileType.PYME))) {

            return creditClient.getCreditsByDocument(client.getDocumentType(), client.getDocumentNumber())
                    .filter(creditLoanDto -> CreditType.CREDIT_CARD.equals(creditLoanDto.getCreditType()))
                    .collectList()
                    .flatMap(list -> {
                        final boolean hasCreditCard = list.stream()
                                .anyMatch(credit -> CreditType.CREDIT_CARD.equals(credit.getCreditType()));
                        log.info("Se reviso en Perfil : {} , hasCreditCard: {}", obj.getProfile(), hasCreditCard);
                        if (hasCreditCard) {
                            return Mono.error(buildException(Constant.PROFILE_NOT_ENABLE));
                        }
                        else { return Mono.just(true); }
                    });

        } else if (ProfileType.VIP.equals(obj.getProfile()) && !ClientType.NATURAL.equals(client.getClientType())) {
            log.info("Perfil VIP, solo puede ser asignado para un cliente Persona Natural");
            return Mono.error(buildException("Perfil VIP, solo puede ser asignado para un cliente Persona Natural"));

        } else if (ProfileType.PYME.equals(obj.getProfile()) && !ClientType.BUSINESS.equals(client.getClientType())) {
            log.info("Perfil PYME, solo puede ser asignado para un cliente Empresa");
            return Mono.error(buildException("Perfil PYME, solo puede ser asignado para un cliente Empresa"));
        }

        return Mono.just(true);
    }

    private Mono<ValidateDto> validateAvailableWithdrawal(AccountEntity accountEntity, WithdrawalRequest withdrawal,
                                                            ProductDto productDto) {
        final Double amount = withdrawal.getAmount();
        if (amount <= 0.0) {
            log.info("el monto debe ser mayor a cero");
            return Mono.error(buildException("En Retiro, el monto debe ser mayor a cero, no procede."));
        }

        if (AccountType.FIXED_DEPOSIT.equals(accountEntity.getAccountType())
                && !accountEntity.getTransactionDayEnable().equals(LocalDate.now().getDayOfMonth())
        ) {
            log.info("CUENTA PLAZO FIJO, hoy no es permitido realizar operacion, deberia ser el "
                    + accountEntity.getTransactionDayEnable());
            return Mono.error(
                    buildException("En Deposito plazo fijo, hoy no es permitido realizar operacion, deberia ser el "
                            + accountEntity.getTransactionDayEnable()));

        }

        final var balance = accountEntity.getBalance();

        if (balance < withdrawal.getAmount()) {
            log.info("No hay saldo en la cuenta para el retiro, balance : {}", balance);
            return Mono.error(buildException("No hay saldo en la cuenta para el retiro, no procede."));
        }

        //validar maxima cantidad de operaciones por mes, si es que esta activa su configuracion
        if (exceedMaximumTransactions(productDto, accountEntity)) {
            log.info("En Deposito, se ha superado la cantidad de movimientos mensuales, aplica Comision");
            return Mono.just(ValidateDto.builder().valid(true).costMovement(true).build());
        }
        return Mono.just(ValidateDto.builder().valid(true).costMovement(false).build());
    }

    private boolean exceedMaximumTransactions(ProductDto productDto, AccountEntity accountEntity) {
        //validar maxima cantidad de operaciones por mes, si es que esta activa su configuracion
        final var lastTransactionDate = accountEntity.getLastTransactionDate();
        if (!productDto.getFeature().getMovement().getUnlimited() && lastTransactionDate != null) {
            final var currentMonth = LocalDateTime.now().getMonthValue();
            final var lastTransactionMonth = lastTransactionDate.getMonthValue();
            final var maximPerMonth = productDto.getFeature().getMovement().getQuantityMaxim();
            if (currentMonth == lastTransactionMonth
                    && accountEntity.getQuantityCurrentTransactionCount() >= maximPerMonth) {
                return true;
            }
        }
        return false;
    }

    private Mono<AccountEntity> executeWithdrawal(AccountEntity accountEntity, WithdrawalRequest withdrawal,
                                                  ProductDto product, ValidateDto validated) {
        log.info("executeWithdrawal :::::::::::::::::::: execute, accountEntity {}, withdrawal {}"
                , accountEntity, withdrawal);

        final var gloss = String.format("%s, %s %.2f", TransactionType.Withdrawal,
                accountEntity.getCurrency(), withdrawal.getAmount());

        return transactionClient.saveWithdrawal(accountEntity.getId(), withdrawal)
                .flatMap(trx -> this.saveCostMovement(validated, accountEntity, trx, product, gloss))
                .flatMap(transactionResponseDto -> {
                    log.info("Pass to update Balance ...");

                    if (accountEntity.getLastTransactionDate() != null) {
                        final var currentMonth = LocalDateTime.now().getMonthValue();
                        final var lastTransactionMonth = accountEntity.getLastTransactionDate().getMonthValue();
                        if (currentMonth != lastTransactionMonth) {
                            accountEntity.setQuantityCurrentTransactionCount(1);
                        } else {
                            //add quantity of transactions
                            final var quantity = accountEntity.getQuantityCurrentTransactionCount() + 1;
                            accountEntity.setQuantityCurrentTransactionCount(quantity);
                        }
                    } else {
                        final var quantity = accountEntity.getQuantityCurrentTransactionCount() + 1;
                        accountEntity.setQuantityCurrentTransactionCount(quantity);
                    }

                    log.info("Balance current : {}", accountEntity.getBalance());

                    var balanceNew = accountEntity.getBalance() - withdrawal.getAmount();
                    if (validated.getCostMovement()) {
                        log.info("Cost Movement : {}", product.getFeature().getMovement().getCost());
                        balanceNew = balanceNew - product.getFeature().getMovement().getCost();
                    }
                    accountEntity.setBalance(balanceNew);

                    log.info("Withdrawal : {}", withdrawal.getAmount());
                    log.info("Balance new : {}", accountEntity.getBalance());

                    accountEntity.setBalance(accountEntity.getBalance() - withdrawal.getAmount());
                    accountEntity.setLastTransactionDate(LocalDateTime.now());
                    accountEntity.setUpdatedAt(LocalDateTime.now());

                    return repository.save(accountEntity)
                            .doOnNext(accountEntity1 -> log.info("Account balance updated, by withdrawal"));

                });
    }

    private Mono<ValidateDto> validateAvailableDeposit(AccountEntity accountEntity,
                                                       DepositRequest deposit, ProductDto productDto) {
        final Double amount = deposit.getAmount();
        if (amount <= 0.0) {
            log.info("el monto debe ser mayor a cero");
            return Mono.error(buildException("En Deposito, el monto debe ser mayor a cero, no procede."));
        }

        if (AccountType.FIXED_DEPOSIT.equals(accountEntity.getAccountType())
                && !accountEntity.getTransactionDayEnable().equals(LocalDate.now().getDayOfMonth())
        ) {
            log.info("CUENTA PLAZO FIJO, hoy no es permitido realizar operacion, deberia ser el "
                    + accountEntity.getTransactionDayEnable());
            return Mono.error(
                    buildException("En Deposito plazo fijo, hoy no es permitido realizar operacion, deberia ser el "
                            + accountEntity.getTransactionDayEnable()));

        }

        //validar maxima cantidad de operaciones por mes, si es que esta activa su configuracion
        if (exceedMaximumTransactions(productDto, accountEntity)) {
            log.info("En Deposito, se ha superado la cantidad de movimientos mensuales, aplica Comision");
            return Mono.just(ValidateDto.builder().valid(true).costMovement(true).build());
        }
        return Mono.just(ValidateDto.builder().valid(true).costMovement(false).build());
    }

    private Mono<TransactionResponseDto> saveCostMovement(ValidateDto validated, AccountEntity accountEntity,
                                                          TransactionResponseDto trx, ProductDto product,
                                                          String gloss) {
        log.info("Movement saved with id : {} to account : {}", trx.getId(), accountEntity.getAccountNumber());
        if (validated.getCostMovement()) {
            final var movement = CommissionDto.builder()
                    .amount(product.getFeature().getMovement().getCost())
                    .entityClient("Commission by " + gloss)
                    .origin(TransactionOrigin.ACCOUNT)
                    .build();
            return transactionClient.saveCommission(accountEntity.getId(), movement)
                    .doOnNext(commission -> {
                        log.info("Commission saved with id : {} to account : {}",
                                commission.getId(), accountEntity.getAccountNumber());
                    });
        }
        return Mono.just(trx);
    }

    private Mono<AccountEntity> executeDeposit(AccountEntity accountEntity, DepositRequest depositRequest,
                                               ProductDto product, ValidateDto validated) {
        log.info("executeDeposit :::::::::::::::::::: execute, accountEntity {}, depositRequest {}"
                , accountEntity, depositRequest);

        final var gloss = String.format("%s, %s %.2f", TransactionType.Deposit,
                accountEntity.getCurrency(), depositRequest.getAmount());

        return transactionClient.saveDeposit(accountEntity.getId(), depositRequest)
                .flatMap(trx -> this.saveCostMovement(validated, accountEntity, trx, product, gloss))
                .flatMap(transactionResponseDto -> {
                    log.info("Pass to update Balance ...");

                    if (accountEntity.getLastTransactionDate() != null) {
                        final var currentMonth = LocalDateTime.now().getMonthValue();
                        final var lastTransactionMonth = accountEntity.getLastTransactionDate().getMonthValue();
                        if (currentMonth != lastTransactionMonth) {
                            accountEntity.setQuantityCurrentTransactionCount(1);
                        } else {
                            //add quantity of transactions
                            final var quantity = accountEntity.getQuantityCurrentTransactionCount() + 1;
                            accountEntity.setQuantityCurrentTransactionCount(quantity);
                        }
                    } else {
                        final var quantity = accountEntity.getQuantityCurrentTransactionCount() + 1;
                        accountEntity.setQuantityCurrentTransactionCount(quantity);
                    }

                    log.info("Balance current : {}", accountEntity.getBalance());

                    var balanceNew = accountEntity.getBalance() + depositRequest.getAmount();
                    if (validated.getCostMovement()) {
                        log.info("Cost Movement : {}", product.getFeature().getMovement().getCost());
                        balanceNew = balanceNew - product.getFeature().getMovement().getCost();
                    }
                    accountEntity.setBalance(balanceNew);

                    log.info("Deposit : {}", depositRequest.getAmount());
                    log.info("Balance new : {}", accountEntity.getBalance());

                    accountEntity.setLastTransactionDate(LocalDateTime.now());
                    accountEntity.setUpdatedAt(LocalDateTime.now());

                    return repository.save(accountEntity)
                            .doOnNext(accountEntity1 -> log.info("Account balance updated, by deposit"));

                });
    }

    private Mono<Boolean> validateFixedDepositAccount(ProductDto product, AccountNewRequest obj) {
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
                .flatMap(repository::save)
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
        final StringBuilder numberAccount = new StringBuilder();
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
