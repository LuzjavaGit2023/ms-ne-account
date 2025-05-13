package pe.com.app.account.controller.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pe.com.app.account.common.config.ClientType;
import pe.com.app.account.common.config.DocumentType;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ClientRequest {
    private DocumentType documentType;
    private String documentNumber;
    private String name;
    private String lastName;
    private ClientType clientType;
}
