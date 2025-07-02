package pe.com.app.account.util;

import pe.com.app.account.common.config.AccountType;
import pe.com.app.account.common.config.ProductType;
import pe.com.app.account.model.dto.product.ProductDto;
import reactor.core.publisher.Flux;

import java.util.List;

public class ProductUtil {

    public static Flux<ProductDto> buildProductDtoListFlux() {
        return Flux.fromIterable(buildProductDtoList());
    }

    public static List<ProductDto> buildProductDtoList() {
        return List.of(getSavingsAccount(), getCurrentAccount(), getFixedDepositAccount());
    }

    public static ProductDto getSavingsAccount() {
        return ProductDto.builder()
                .id("100P")
                .productType(ProductType.LIABILITY.getDescription())
                .label("Product " + AccountType.SAVINGS_ACCOUNT.getDescription())
                .productType(AccountType.SAVINGS_ACCOUNT.getDescription())
                .build();
    }

    public static ProductDto getCurrentAccount() {
        return ProductDto.builder()
                .id("200P")
                .productType(ProductType.LIABILITY.getDescription())
                .label("Product " + AccountType.CURRENT_ACCOUNT.getDescription())
                .productType(AccountType.CURRENT_ACCOUNT.getDescription())
                .build();
    }

    public static ProductDto getFixedDepositAccount() {
        return ProductDto.builder()
                .id("300P")
                .productType(ProductType.LIABILITY.getDescription())
                .label("Product " + AccountType.FIXED_DEPOSIT.getDescription())
                .productType(AccountType.FIXED_DEPOSIT.getDescription())
                .build();
    }
}
