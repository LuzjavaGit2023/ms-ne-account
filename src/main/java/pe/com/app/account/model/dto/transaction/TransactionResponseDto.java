package pe.com.app.account.model.dto.transaction;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class TransactionResponseDto implements Serializable {
    private static final long serialVersionUID = 3950859190381403285L;
    String id;
}
