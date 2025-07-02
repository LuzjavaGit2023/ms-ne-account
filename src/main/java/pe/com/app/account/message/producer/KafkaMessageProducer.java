package pe.com.app.account.message.producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import pe.com.app.account.message.UtiMessage;
import pe.com.app.account.model.dto.transaction.TransactionRequestDto;
import reactor.core.publisher.Mono;

@Service
public class KafkaMessageProducer {

    private final KafkaTemplate<String, TransactionRequestDto> kafkaTemplate;

    @Value("${app.kafka.topic.transaction-requested}")
    private String topic;

    public KafkaMessageProducer(KafkaTemplate<String, TransactionRequestDto> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public Mono<Void> sendMessage(TransactionRequestDto transaction) {
        return Mono.fromFuture(
                kafkaTemplate.send(topic, UtiMessage.getCurrentDateTimeString(), transaction)
                        .completable()
                )
                .then();
    }
}