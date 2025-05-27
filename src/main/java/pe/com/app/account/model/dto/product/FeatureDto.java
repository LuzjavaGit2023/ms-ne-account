package pe.com.app.account.model.dto.product;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class FeatureDto implements Serializable {
    private static final long serialVersionUID = 8909396096289476165L;
    private CommissionDto commission;
    private MovementDto movement;
    private CreditDto credit;
}
