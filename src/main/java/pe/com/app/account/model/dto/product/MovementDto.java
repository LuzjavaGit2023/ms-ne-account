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
public class MovementDto implements Serializable {
    private static final long serialVersionUID = 3873680094566412922L;
    private Boolean unlimited;
    private Integer quantityMaxim;
    private Boolean specificDay;
    private Double cost;
}
