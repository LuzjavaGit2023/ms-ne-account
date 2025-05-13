package pe.com.app.account.webclient.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.com.app.account.model.dto.product.ProductDto;
import pe.com.app.account.webclient.BankingMovementClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class BankingMovementClientImpl implements BankingMovementClient {

    @Override
    public Mono<ProductDto> addBankingMovement(String id) {
        return null;
    }

}
