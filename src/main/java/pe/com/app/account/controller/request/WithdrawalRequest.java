package pe.com.app.account.controller.request;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pe.com.app.account.common.config.Currency;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class WithdrawalRequest implements Serializable {
    private static final long serialVersionUID = 3295445481373723206L;
    private Double amount;
    private Currency currency; // Moneda (USD, PEN, EUR, etc.)

    private String entityClient;

}
