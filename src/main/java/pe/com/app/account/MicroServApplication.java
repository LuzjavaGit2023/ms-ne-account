package pe.com.app.account;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "API Account", version = "1.0",
        description = "Reactive API for account management")
)
public class MicroServApplication {

    public static void main(String[] args) {
        SpringApplication.run(MicroServApplication.class, args);
    }

}
