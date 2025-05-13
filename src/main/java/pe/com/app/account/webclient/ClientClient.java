package pe.com.app.account.webclient;

import pe.com.app.account.common.config.DocumentType;
import pe.com.app.account.controller.request.ClientRequest;
import pe.com.app.account.model.dto.client.ClientDto;
import reactor.core.publisher.Mono;

public interface ClientClient {
    Mono<ClientDto> getClient(String id);
    Mono<Void> addNewClient(ClientRequest obj);
}
