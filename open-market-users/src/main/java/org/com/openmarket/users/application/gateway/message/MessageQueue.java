package org.com.openmarket.users.application.gateway.message;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.com.openmarket.users.application.gateway.message.dto.CommonMessageDTO;
import org.com.openmarket.users.core.domain.repository.PastMessagesRepository;
import org.com.openmarket.users.infra.persistence.entity.PastMessagesEntity;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

import static org.com.openmarket.users.application.config.constants.QueueConstants.ItemService.ITEM_QUEUE;
import static org.com.openmarket.users.application.config.constants.QueueConstants.MarketService.MARKET_QUEUE;
import static org.com.openmarket.users.application.config.constants.QueueConstants.User.USER_QUEUE;
import static org.com.openmarket.users.application.config.constants.QueueConstants.WalletService.WALLET_QUEUE;

@Slf4j
@Component
@AllArgsConstructor
public class MessageQueue {
    private final static ObjectMapper mapper = new ObjectMapper();

    private final RabbitTemplate rabbitTemplate;
    private final PastMessagesRepository pastMessagesRepository;

    @RabbitListener(queues = {USER_QUEUE})
    public void execute(@Payload Message messagePayload) {
        try {
            log.info("New message received on queue {}", USER_QUEUE);
            String bodyConverted = convertBodyAsString(messagePayload);
            List<String> queuesToReceive = List.of(USER_QUEUE, ITEM_QUEUE, WALLET_QUEUE, MARKET_QUEUE);
            registerMessageOnConfigDb(bodyConverted, queuesToReceive);

            CommonMessageDTO messageDTO = mapper.readValue(bodyConverted, CommonMessageDTO.class);

            for (String queue : queuesToReceive) {
                if (!queue.equals(USER_QUEUE)) {
                    rabbitTemplate.convertAndSend(queue, mapper.writeValueAsString(messageDTO));
                }
            }
        } catch (Exception e) {
            String message = "Error while receiving a new message";
            log.error(message, e);
            throw new RuntimeException(message);
        }
    }

    private void registerMessageOnConfigDb(String data, List<String> queuesToReceive) {
        PastMessagesEntity message = new PastMessagesEntity();
        message.setQueueName(USER_QUEUE);
        message.setData(data);
        message.setCreatedAt(new Date().toString());
        message.setQueuesAlreadyReceived(queuesToReceive);

        pastMessagesRepository.insertMessageRegistry(message);
    }

    private String convertBodyAsString(Message messagePayload) {
        byte[] body = messagePayload.getBody();
        return new String(body);
    }
}