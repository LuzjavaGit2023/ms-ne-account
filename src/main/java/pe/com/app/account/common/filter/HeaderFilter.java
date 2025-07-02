package pe.com.app.account.common.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class HeaderFilter implements WebFilter {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        final ServerHttpRequest request = exchange.getRequest();
        log.info("===== New Request API =====");
        log.info("Method: {}", request.getMethod());
        log.info("URI: {}", request.getURI());
        request.getHeaders().forEach((key, value) -> {
            log.info("Header, {} : {}", key, value);
        });
        return chain.filter(exchange);
    }
}
