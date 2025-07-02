package pe.com.app.account.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.com.app.account.repository.AccountRepository;
import pe.com.app.account.webclient.ClientClient;
import pe.com.app.account.webclient.CreditClient;
import pe.com.app.account.webclient.ProductClient;
import pe.com.app.account.webclient.TransactionClient;

@ExtendWith(MockitoExtension.class)
public class DeleteCaseTest {

    @InjectMocks
    private AccountServiceImpl service;
    @Mock
    private AccountRepository repository;
    @Mock
    private ClientClient clientClient;
    @Mock
    private ProductClient productClient;
    @Mock
    private TransactionClient transactionClient;
    @Mock
    private CreditClient creditClient;

    @BeforeEach
    void setup() {

    }

    @Test
    void givenDeleteRequest_whenDeleteAccount_thenReturnSuccess() {
    }

    @Test
    void givenDeleteRequest_whenNotFount_thenReturnError() {
    }

    @Test
    void givenDeleteRequest_whenNotActive_thenReturnError() {
    }

}
