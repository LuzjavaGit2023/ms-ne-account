package pe.com.app.account.webclient;

import pe.com.app.account.model.dto.product.ProductDto;
import reactor.core.publisher.Mono;

public interface ProductClient {
    Mono<ProductDto> getProduct(String id);
}
