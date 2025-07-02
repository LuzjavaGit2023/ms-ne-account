package pe.com.app.account.model.dto.transaction;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pe.com.app.account.common.config.OfferType;
import pe.com.app.account.common.config.PaymentModeP2p;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class TransactionRequestDto implements Serializable {
    private static final long serialVersionUID = -6448844369029641586L;
    private String serviceId;
    private OfferType offerType;
    private PaymentModeP2p paymentMode;
    private Double amount;
}
