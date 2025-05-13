package pe.com.app.account.model.dto.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CreditDto implements Serializable {
    private static final long serialVersionUID = -7363006836523138467L;
    private Boolean unlimited;
    private Integer quantityMaxim;
}
