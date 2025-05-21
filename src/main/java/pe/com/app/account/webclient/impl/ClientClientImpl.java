package pe.com.app.account.webclient.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import pe.com.app.account.advice.ErrorResponse;
import pe.com.app.account.common.config.DocumentType;
import pe.com.app.account.controller.request.ClientRequest;
import pe.com.app.account.model.dto.client.ClientDto;
import pe.com.app.account.webclient.ClientClient;
import pe.com.app.account.webclient.config.ClientServiceConfig;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ClientClientImpl implements ClientClient {

    @Autowired
    @Qualifier("clientWebToClient")
    private WebClient clientWeb;

    private final ClientServiceConfig config;

    @Override
    public Mono<ClientDto> getClient(String id) {
        return clientWeb.get()
                .uri(config.getSearchById(), id)
                .retrieve()
                .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
                        response -> buildError(response))
                .bodyToMono(ClientDto.class);
    }

    @Override
    public Mono<ClientDto> getClientByDocument(DocumentType documentType, String documentNumber) {
        return clientWeb.get()
                .uri(config.getSearchByDocument(), documentType, documentNumber)
                .retrieve()
                .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
                        response -> buildError(response))
                .bodyToMono(ClientDto.class);
    }

    @Override
    public Mono<Void> addNewClient(ClientRequest obj) {
        return null;
    }

    private Mono<IllegalStateException> buildError(ClientResponse response) {
        return response.bodyToMono(ErrorResponse.class)
                .flatMap(errorJson -> Mono.error(
                        new IllegalStateException("Api Client, " + errorJson.getMessage().split(":")[1].trim())
                ));
    }
}
