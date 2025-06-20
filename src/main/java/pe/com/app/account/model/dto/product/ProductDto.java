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
public class ProductDto implements Serializable {
    private static final long serialVersionUID = 7346145359381795413L;
    private String id;
    private String productType;
    private String productSubType;
    private String label;
    private String description;
    private FeatureDto feature;
}
