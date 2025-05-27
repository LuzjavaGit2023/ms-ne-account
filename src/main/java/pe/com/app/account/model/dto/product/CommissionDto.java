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
public class CommissionDto implements Serializable {
    private static final long serialVersionUID = 534097346958778080L;
    private Boolean free;
    private Double cost;
}
