package pe.com.app.account.webclient;

import pe.com.app.account.controller.request.DepositRequest;
import pe.com.app.account.controller.request.WithdrawalRequest;
import pe.com.app.account.model.dto.transaction.CommissionDto;
import pe.com.app.account.model.dto.transaction.TransactionResponseDto;
import reactor.core.publisher.Mono;

public interface TransactionClient {

    Mono<TransactionResponseDto> saveDeposit(String creditId, DepositRequest deposit);

    Mono<TransactionResponseDto> saveWithdrawal(String creditId, WithdrawalRequest consumption);
    Mono<TransactionResponseDto> saveCommission(String creditId, CommissionDto consumption);

}
