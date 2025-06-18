package pe.com.app.account.webclient.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import pe.com.app.account.advice.ErrorResponse;
import pe.com.app.account.controller.request.DepositRequest;
import pe.com.app.account.controller.request.WithdrawalRequest;
import pe.com.app.account.model.dto.transaction.CommissionDto;
import pe.com.app.account.model.dto.transaction.TransactionResponseDto;
import pe.com.app.account.webclient.TransactionClient;
import pe.com.app.account.webclient.config.TransactionServiceConfig;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class TransactionClientImpl implements TransactionClient {

    @Autowired
    @Qualifier("clientWebToTransaction")
    private WebClient clientWeb;

    private final TransactionServiceConfig config;
    @Override
    public Mono<TransactionResponseDto> saveDeposit(String creditId, DepositRequest request) {
        return clientWeb.post()
                .uri(config.getSaveDeposit(), creditId)
                .bodyValue(request)
                .retrieve()
                .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
                        response -> buildError(response))
                .bodyToMono(TransactionResponseDto.class);
    }

    @Override
    public Mono<TransactionResponseDto> saveWithdrawal(String creditId, WithdrawalRequest request) {
        return clientWeb.post()
                .uri(config.getSaveWithdrawal(), creditId)
                .bodyValue(request)
                .retrieve()
                .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
                        response -> buildError(response))
                .bodyToMono(TransactionResponseDto.class);
    }

    @Override
    public Mono<TransactionResponseDto> saveCommission(String creditId, CommissionDto request) {
        return clientWeb.post()
                .uri(config.getSaveCommission(), creditId)
                .bodyValue(request)
                .retrieve()
                .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
                        response -> buildError(response))
                .bodyToMono(TransactionResponseDto.class);
    }

    private Mono<IllegalStateException> buildError(ClientResponse response) {
        return response.bodyToMono(ErrorResponse.class)
                .flatMap(errorJson -> Mono.error(
                        new IllegalStateException("Api Transaction, " + errorJson.getMessage().split(":")[1].trim())
                ));
    }
}
