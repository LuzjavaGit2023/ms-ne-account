package pe.com.app.account.util;

import pe.com.app.account.common.config.AccountType;
import pe.com.app.account.common.config.ProfileType;
import pe.com.app.account.model.dto.AccountDto;

import java.time.LocalDateTime;
import java.util.List;

public class AccountUtil {

    private List<AccountDto> buildAccountDtoGetAllList() {
        var list = buildAccountDtoDniList();
        list.addAll(buildAccountDtoRucList());
        return list;
    }

    private List<AccountDto> buildAccountDtoDniList() {
        return List.of( getCurrentAccountDni(), getSavingsAccountDni(), getFixedDepositAccountDni());
    }

    private List<AccountDto> buildAccountDtoRucList() {
        return List.of( getCurrentAccountRuc());
    }


    public AccountDto getCurrentAccountRuc() {
        return AccountDto.builder()
                .id("099A")
                .clientId(ClientUtil.buildClientRuc().getId())
                .productId(ProductUtil.getCurrentAccount().getId())
                .profile(ProfileType.GENERAL)
                .accountNumber("111-555-4445-5555555")
                .accountType(AccountType.CURRENT_ACCOUNT)
                .balance(100d)
                .createdAt(LocalDateTime.now())
                .build();
    }

    public AccountDto getCurrentAccountDni() {
        return AccountDto.builder()
                .id("100A")
                .clientId(ClientUtil.buildClientDni().getId())
                .productId(ProductUtil.getCurrentAccount().getId())
                .profile(ProfileType.GENERAL)
                .accountNumber("111-555-4445-5555555")
                .accountType(AccountType.CURRENT_ACCOUNT)
                .balance(100d)
                .createdAt(LocalDateTime.now())
                .build();
    }

    public AccountDto getSavingsAccountDni() {
        return AccountDto.builder()
                .id("101A")
                .clientId(ClientUtil.buildClientDni().getId())
                .productId(ProductUtil.getSavingsAccount().getId())
                .profile(ProfileType.GENERAL)
                .accountNumber("111-555-4445-5555555")
                .accountType(AccountType.SAVINGS_ACCOUNT)
                .balance(100d)
                .createdAt(LocalDateTime.now())
                .build();
    }

    public AccountDto getFixedDepositAccountDni() {
        return  AccountDto.builder()
                .id("100A")
                .clientId(ClientUtil.buildClientDni().getId())
                .productId(ProductUtil.getFixedDepositAccount().getId())
                .profile(ProfileType.GENERAL)
                .accountNumber("111-555-4445-5555555")
                .accountType(AccountType.FIXED_DEPOSIT)
                .balance(5000d)
                .createdAt(LocalDateTime.now())
                .build();
    }
}
