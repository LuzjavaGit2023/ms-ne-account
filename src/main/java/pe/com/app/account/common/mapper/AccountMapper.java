package pe.com.app.account.common.mapper;

import org.springframework.util.CollectionUtils;
import pe.com.app.account.common.config.AccountStatus;
import pe.com.app.account.common.config.AccountType;
import pe.com.app.account.controller.request.AccountNewRequest;
import pe.com.app.account.controller.request.AccountUpdateRequest;
import pe.com.app.account.controller.response.AccountResponse;
import pe.com.app.account.model.dto.product.ProductDto;
import pe.com.app.account.model.persistence.AccountEntity;

import java.time.LocalDateTime;

public class AccountMapper {

    public static AccountEntity buildEntityNew(AccountNewRequest a, ProductDto productDto){
        return AccountEntity.builder()
                .productId(a.getProductId())
                .clientId(a.getClientId())
                .accountType(AccountType.fromString(productDto.getProductSubType()))
                .currency(a.getCurrency())
                .createdAt(LocalDateTime.now())
                .status(AccountStatus.ACTIVO)
                .balance(Double.valueOf(0.0))
                .quantityCurrentTransactionCount(Integer.valueOf(0))
                .transactionDayEnable(AccountType.FIXED_DEPOSIT.equals(AccountType.fromString(productDto.getProductSubType()))
                        ? a.getTransactionDayEnable() : null)
                .headlines(a.getHeadlines())
                .signatories(a.getSignatories())
                .build();
    }

    public static AccountResponse buildAccountResponse(AccountEntity e){
        return AccountResponse.builder()
                .id(e.getId())
                .productId(e.getProductId())
                .clientId(e.getClientId())
                .accountType(e.getAccountType())
                .accountNumber(e.getAccountNumber())
                .currency(e.getCurrency())
                .createdAt(LocalDateTime.now())
                .status(e.getStatus())
                .balance(e.getBalance())
                .quantityCurrentTransactionCount(e.getQuantityCurrentTransactionCount())
                .transactionDayEnable(e.getTransactionDayEnable())
                .headlines(e.getHeadlines())
                .signatories(e.getSignatories())
                .build();
    }

    public static AccountEntity buildEntityUpdate(AccountEntity currentAccount, AccountUpdateRequest updateAccount) {
        if (updateAccount.getTransactionDayEnable() != null && updateAccount.getTransactionDayEnable() > 0) {
            currentAccount.setTransactionDayEnable(updateAccount.getTransactionDayEnable());
        }
        if (!CollectionUtils.isEmpty(updateAccount.getHeadlines())) {
            currentAccount.setHeadlines(updateAccount.getHeadlines());
        }
        if (updateAccount.getSignatories() != null) {
            currentAccount.setSignatories(updateAccount.getSignatories());
        }
        currentAccount.setUpdatedAt(LocalDateTime.now());
        return currentAccount;
    }

    public static AccountEntity buildEntityDelete(AccountEntity currentAccount) {
        currentAccount.setStatus(AccountStatus.CERRADO);
        currentAccount.setUpdatedAt(LocalDateTime.now());
        return currentAccount;
    }
}
