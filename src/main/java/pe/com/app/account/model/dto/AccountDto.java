package pe.com.app.account.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pe.com.app.account.common.config.ClientType;
import pe.com.app.account.common.config.DocumentType;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AccountDto implements Serializable {
    private String id;
    private DocumentType documentType;
    private String documentNumber;
    private String name;
    private String lastName;
    private ClientType clientType;
}
