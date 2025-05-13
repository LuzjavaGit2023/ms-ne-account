package pe.com.app.account.webclient;

import pe.com.app.account.model.dto.product.ProductDto;
import reactor.core.publisher.Mono;

public interface BankingMovementClient {
    Mono<ProductDto> addBankingMovement(String id);
}
