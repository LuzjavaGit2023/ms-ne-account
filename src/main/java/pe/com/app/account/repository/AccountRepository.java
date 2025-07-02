package pe.com.app.account.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import pe.com.app.account.common.config.AccountType;
import pe.com.app.account.common.config.ProfileType;
import pe.com.app.account.model.persistence.AccountEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * <b>Interface</b>: AccountRepository <br/>
 * <b>Copyright</b>: 2025 Tu Banco - Celula <br/>
 * .
 *
 * @author 2025 Tu Banco - Peru <br/>
 * <u>Service Provider</u>: Tu Banco <br/>
 * <u>Changes:</u><br/>
 * <ul>
 * <li>
 * May 10, 2025 Creación de Clase.
 * </li>
 * </ul>
 */
public interface AccountRepository extends ReactiveMongoRepository<AccountEntity, String> {

    Mono<Long> countByClientIdAndAccountType(String id, AccountType clientType);
    Flux<AccountEntity> findByClientId(String clientId);
    Flux<AccountEntity> findByAccountType(AccountType clientType);

    Mono<AccountEntity> findByAccountNumber(String accountNumber);
    Mono<Long> countByClientIdAndProfile(String id, ProfileType profile);
}
