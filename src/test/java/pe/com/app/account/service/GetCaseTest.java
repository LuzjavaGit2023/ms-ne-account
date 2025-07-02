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
public class GetCaseTest {

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
    void givenGetAllByDocumentRequest_whenGetAllByDocument_thenReturnSuccess() {
    }

    @Test
    void givenGetByIdRequest_whenGetById_thenReturnSuccess() {
    }

    @Test
    void givenGetByIdRequest_whenNotFound_thenReturnError() {
    }

    @Test
    void givenGetByIdRequest_whenNotActive_thenReturnError() {
    }

}
