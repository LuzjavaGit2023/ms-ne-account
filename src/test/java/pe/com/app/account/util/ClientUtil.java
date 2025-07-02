package pe.com.app.account.util;

import pe.com.app.account.common.config.ClientType;
import pe.com.app.account.common.config.DocumentType;
import pe.com.app.account.model.dto.client.ClientDto;

import java.util.List;

public class ClientUtil {

    public static List<ClientDto> buildClientList() {
        return List.of(buildClientDni(), buildClientRuc());
    }

    public static ClientDto buildClientDni() {
        return ClientDto.builder()
                .documentType(DocumentType.DNI)
                .documentNumber("44745520")
                .name("Jorge Lucas")
                .lastName("Cardenas Neto")
                .clientType(ClientType.NATURAL)
                .build();
    }

    public static ClientDto buildClientRuc() {
        return ClientDto.builder()
                .documentType(DocumentType.RUC)
                .documentNumber("10556211009")
                .name("ROCO SAC")
                .lastName("")
                .clientType(ClientType.BUSINESS)
                .build();
    }
}
