package pe.com.app.account.controller.response;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import pe.com.app.account.model.dto.AccountDto;

@Getter
@Setter
@SuperBuilder
public class AccountResponse extends AccountDto implements Serializable {

    private static final long serialVersionUID = -4835095220588061994L;
}
