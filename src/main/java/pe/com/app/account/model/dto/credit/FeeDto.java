package pe.com.app.account.model.dto.credit;

import java.time.LocalDate;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pe.com.app.account.common.config.FeeStatusType;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class FeeDto {
    private Integer order;
    private UUID uuid;
    private LocalDate paymentDate;
    private Double amount;
    private FeeStatusType status;
    private LocalDate paidDate;
}
