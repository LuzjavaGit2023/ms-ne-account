package pe.com.app.account.webclient.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient clientWebToClient(ClientServiceConfig config) {
        return WebClient.builder()
                .baseUrl(config.getUrl())
                .build();
    }

    @Bean
    public WebClient clientWebToProduct(ProductServiceConfig config) {
        return WebClient.builder()
                .baseUrl(config.getUrl())
                .build();
    }

}
