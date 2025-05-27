package pe.com.app.account.advice;

import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ServerWebInputException;
import pe.com.app.account.common.util.Constant;
import reactor.core.publisher.Mono;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(ServerWebInputException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleInputException(ServerWebInputException ex) {
        log.error("Start GlobalExceptionHandler", ex);
        final var error = ErrorResponse.builder()
                .error(Constant.ERROR_CODE)
                .message("Error de entrada: " + ex.getReason())
                .timestamp(LocalDateTime.now().toString())
                .build();
        return Mono.just(ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error));
    }

    @ExceptionHandler(RecursoNoEncontradoException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleNotFound(RecursoNoEncontradoException ex) {
        log.error("Start GlobalExceptionHandler", ex);
        final var error = ErrorResponse.builder()
                .error(Constant.ERROR_CODE)
                .message("No encontrado: " + ex.getMessage())
                .timestamp(LocalDateTime.now().toString())
                .build();
        return Mono.just(ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(error));
    }

    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<ErrorResponse>> handleGeneric(Exception ex) {
        log.error("Start GlobalExceptionHandler", ex);
        final var error = ErrorResponse.builder()
                .error(Constant.ERROR_CODE)
                .message("Error interno: " + ex.getMessage())
                .timestamp(LocalDateTime.now().toString())
                .build();
        return Mono.just(ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(error));
    }
}
