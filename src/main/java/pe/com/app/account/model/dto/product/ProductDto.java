package pe.com.app.account.model.dto.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ProductDto implements Serializable {
    private static final long serialVersionUID = 7346145359381795413L;
    @Id
    private String id;
    private String productType;
    private String productSubType;
    private String label;
    private String description;
    private FeatureDto feature;
}
