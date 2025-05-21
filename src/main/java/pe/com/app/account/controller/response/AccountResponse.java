package pe.com.app.account.controller.response;

import lombok.*;
import lombok.experimental.SuperBuilder;
import pe.com.app.account.model.dto.AccountDto;

import java.io.Serializable;

@Getter
@Setter
@SuperBuilder
public class AccountResponse extends AccountDto implements Serializable {

    private static final long serialVersionUID = -4835095220588061994L;
}
