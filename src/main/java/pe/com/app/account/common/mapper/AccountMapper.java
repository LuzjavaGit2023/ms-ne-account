package pe.com.app.account.common.mapper;

import pe.com.app.account.common.config.AccountStatus;
import pe.com.app.account.controller.request.AccountNewRequest;
import pe.com.app.account.model.persistence.AccountEntity;

import java.time.LocalDateTime;

public class AccountMapper {

    public static AccountEntity buildEntityNew(AccountNewRequest a){
        return AccountEntity.builder()
                .productId(a.getProductId())
                .clientId(a.getClientId())
                .accountType(a.getAccountType())
                .currency(a.getCurrency())
                .createdAt(LocalDateTime.now())
                .status(AccountStatus.ACTIVO)
                .balance(Double.valueOf(0.0))
                .quantityCurrentTransactionCount(Integer.valueOf(0))
                .transactionDayEnable(a.getTransactionDayEnable())
                .headlines(a.getHeadlines())
                .signatories(a.getSignatories())
                .build();
    }
}
