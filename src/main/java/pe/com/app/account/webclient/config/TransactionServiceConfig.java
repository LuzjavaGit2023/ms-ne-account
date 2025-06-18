package pe.com.app.account.webclient.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "transaction.service")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TransactionServiceConfig {

    private String url;
    private String saveDeposit;
    private String saveWithdrawal;
    private String saveCommission;

}
