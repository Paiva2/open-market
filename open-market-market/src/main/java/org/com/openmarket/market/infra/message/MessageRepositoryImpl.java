package org.com.openmarket.market.infra.message;

import lombok.AllArgsConstructor;
import org.com.openmarket.market.domain.interfaces.MessageRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class MessageRepositoryImpl implements MessageRepository {
    private final RabbitTemplate rabbitTemplate;

    @Override
    public void sendMessage(String queueName, String data) {
        rabbitTemplate.convertAndSend(queueName, data);
    }
}
