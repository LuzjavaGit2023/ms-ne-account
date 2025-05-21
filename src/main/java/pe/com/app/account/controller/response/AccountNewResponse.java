package pe.com.app.account.controller.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pe.com.app.account.common.config.AccountType;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AccountNewResponse {
    private String id;
    private String productId;
    private String clientId;
    private String accountNumber;
    private AccountType accountType;
}
