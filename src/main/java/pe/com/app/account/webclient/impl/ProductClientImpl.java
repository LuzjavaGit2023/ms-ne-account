package pe.com.app.account.webclient.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import pe.com.app.account.advice.ErrorResponse;
import pe.com.app.account.model.dto.product.ProductDto;
import pe.com.app.account.webclient.ProductClient;
import pe.com.app.account.webclient.config.ProductServiceConfig;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ProductClientImpl implements ProductClient {

    @Autowired
    @Qualifier("clientWebToProduct")
    private WebClient clientWeb;

    private final ProductServiceConfig config;

    @Override
    public Mono<ProductDto> getProduct(String id) {
        return clientWeb.get()
                .uri(config.getSearchById(), id)
                .retrieve()
                .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
                        response -> buildError(response))
                .bodyToMono(ProductDto.class);
    }

    private Mono<IllegalStateException> buildError(ClientResponse response) {
        return response.bodyToMono(ErrorResponse.class)
                .flatMap(errorJson -> Mono.error(
                        new IllegalStateException("Api Product, " + errorJson.getMessage().split(":")[1].trim())
                ));
    }
}
