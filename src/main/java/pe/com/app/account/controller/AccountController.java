package pe.com.app.account.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import pe.com.app.account.advice.ErrorResponse;
import pe.com.app.account.common.config.DocumentType;
import pe.com.app.account.controller.request.AccountNewRequest;
import pe.com.app.account.controller.request.AccountUpdateRequest;
import pe.com.app.account.controller.request.DepositRequest;
import pe.com.app.account.controller.request.WithdrawalRequest;
import pe.com.app.account.controller.response.AccountResponse;
import pe.com.app.account.service.AccountService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * <b>Class</b>: AccountController <br/>
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
@RequestMapping("/accounts")
@Tag(name = "Accounts", description = "Functional operations related to accounts")
public class AccountController {

    private final AccountService service;

    /**
     * This method is used to create a new account element.
     *
     * @return Void Mono.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "This method is used to create a new account element.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    public Mono<Void> newAccount(@RequestBody AccountNewRequest request) {
        return service.newAccount(request);
    }

    /**
     * This method is used to list all account elements of client by document.
     *
     * @return AccountResponse Flux.
     */
    @GetMapping("/{documentType}/{documentNumber}")
    @Operation(summary = "This method is used to list all account elements of client by document.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    public Flux<AccountResponse> getAllAccountsByDocument(@PathVariable DocumentType documentType, @PathVariable String documentNumber) {
        return service.getAllAccountsByDocument(documentType, documentNumber);
    }

    /**
     * This method is used to update an account element.
     *
     * @return Void Mono.
     */
    @PatchMapping("/{accountNumber}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "This method is used to update an account element.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successful operation"),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    public Mono<Void> updateAccount(@PathVariable String accountNumber, @RequestBody AccountUpdateRequest obj) {
        return service.updateAccount(accountNumber, obj);
    }

    /**
     * This method is used to delete an account element.
     *
     * @return Void Mono.
     */
    @DeleteMapping("/{accountNumber}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "This method is used to delete an account element.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successful operation"),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    public Mono<Void> deleteAccount(@PathVariable String accountNumber) {

        return service.deleteAccount(accountNumber);
    }

    /**
     * This method is used to save a deposit to account element.
     *
     * @return Void Mono.
     */
    @PostMapping("/{accountNumber}/deposit")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "This method is used to save a deposit to account element.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successful operation"),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    public Mono<Void> depositAccount(@PathVariable String accountNumber, @RequestBody DepositRequest deposit) {
        return service.depositAccount(accountNumber, deposit);
    }

    /**
     * This method is used to save a withdrawal to an account element.
     *
     * @return Void Mono.
     */
    @PostMapping("/{accountNumber}/withdrawal")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "This method is used to save a withdrawal to an account element.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successful operation"),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    public Mono<Void> withdrawalAccount(@PathVariable String accountNumber, @RequestBody WithdrawalRequest withdrawal) {
        return service.withdrawalAccount(accountNumber, withdrawal);
    }
}
