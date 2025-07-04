package pe.com.app.account.model.dto.client;

import java.io.Serializable;
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
public class ClientDto implements Serializable {
    private static final long serialVersionUID = -7191926621739830732L;
    private String id;
    private DocumentType documentType;
    private String documentNumber;
    private String name;
    private String lastName;
    private ClientType clientType;
    private String cell;
    private String email;
}
