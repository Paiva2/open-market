package org.com.openmarket.items.infra.message.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.com.openmarket.items.core.domain.interfaces.repository.MessageRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class MessageRepositoryImpl implements MessageRepository {
    private final static ObjectMapper mapper = new ObjectMapper();

    private final RabbitTemplate rabbitTemplate;

    @Override
    public void sendMessage(String topicExchange, String routingKey, String message) {
        rabbitTemplate.convertAndSend(topicExchange, routingKey, message);
    }

    @Override
    public void sendMessage(String queueName, String message) {
        try {
            rabbitTemplate.convertAndSend(queueName, message);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
