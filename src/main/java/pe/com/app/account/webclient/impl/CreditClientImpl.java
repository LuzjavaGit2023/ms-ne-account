package pe.com.app.account.webclient.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import pe.com.app.account.advice.ErrorResponse;
import pe.com.app.account.common.config.DocumentType;
import pe.com.app.account.model.dto.credit.CreditLoanDto;
import pe.com.app.account.webclient.CreditClient;
import pe.com.app.account.webclient.config.CreditServiceConfig;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CreditClientImpl implements CreditClient {

    @Autowired
    @Qualifier("clientWebToCredit")
    private WebClient clientWeb;

    private final CreditServiceConfig config;

    @Override
    public Flux<CreditLoanDto> getCreditsByDocument(DocumentType documentType, String documentNumber) {
        return clientWeb.get()
                .uri(config.getSearchByDocument(), documentType, documentNumber)
                .retrieve()
                .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
                        response -> buildError(response))
                .bodyToFlux(CreditLoanDto.class);
    }

    private Mono<IllegalStateException> buildError(ClientResponse response) {
        return response.bodyToMono(ErrorResponse.class)
                .flatMap(errorJson -> Mono.error(
                        new IllegalStateException("Api Credit, " + errorJson.getMessage().split(":")[1].trim())
                ));
    }
}
