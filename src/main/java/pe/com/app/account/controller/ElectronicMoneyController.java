package pe.com.app.account.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pe.com.app.account.advice.ErrorResponse;
import pe.com.app.account.controller.response.AccountResponse;
import pe.com.app.account.model.dto.transaction.TransactionRequestDto;
import pe.com.app.account.message.producer.KafkaMessageProducer;
import pe.com.app.account.service.AccountService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * <b>Class</b>: ElectronicMoneyController <br/>
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
@RestController
@RequiredArgsConstructor
@RequestMapping("/virtual-money")
@Tag(name = "ElectronicMoney", description = "Functional operations related to Electronic Money")
public class ElectronicMoneyController {
    private final KafkaMessageProducer messageProducer;
    private final AccountService service;

    @PostMapping("/request")
    public Mono<Void> requestTransaction(@RequestBody TransactionRequestDto transaction) {
        return messageProducer.sendMessage(transaction);
    }

    /**
     * This method is used to list all account enabled to B2B.
     *
     * @return AccountResponse Flux.
     */
    @GetMapping("/clients")
    @Operation(summary = "This method is used to list all account enabled to B2B(electronic-money).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    public Flux<AccountResponse> getAllAccountsToTrxP2P() {
        return service.getAllAccountsToTrxP2P();
    }

}
