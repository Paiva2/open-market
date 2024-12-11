package org.com.openmarket.users.application.gateway.message.userQueue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.com.openmarket.users.application.gateway.message.userQueue.dto.UserCreatedMessageDTO;
import org.com.openmarket.users.core.domain.repository.UserDataMessageRepository;
import org.com.openmarket.users.infra.persistence.entity.UserDataMessageEntity;
import org.com.openmarket.users.infra.persistence.repository.userDataMessage.UserDataMessageRepositoryImpl;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.com.openmarket.users.application.config.constants.QueueConstants.User.USER_DATA_QUEUE;
import static org.com.openmarket.users.application.config.constants.QueueConstants.UserItem.*;
import static org.com.openmarket.users.application.config.constants.QueueConstants.UserWallet.*;

//todo: make DLQ
@Slf4j
@Component
@AllArgsConstructor
public class UserDataMessageQueue {
    private final static ObjectMapper mapper = new ObjectMapper();
    private final RabbitTemplate rabbitTemplate;
    private final UserDataMessageRepository userDataMessageRepository;

    @RabbitListener(queues = {USER_DATA_QUEUE})
    public void execute(@Payload Message messagePayload) {
        try {
            log.info("New message received on queue {}", USER_DATA_QUEUE);
            String bodyConverted = convertBodyAsString(messagePayload);
            registerMessageOnConfigDb(bodyConverted);
            sendToItemServiceQueue(bodyConverted);
            sendToWalletServiceQueue(bodyConverted);
        } catch (Exception e) {
            String message = "Error while receiving a new message";
            log.error(message, e);
            throw new RuntimeException(message);
        }
    }

    private void registerMessageOnConfigDb(String data) {
        UserDataMessageEntity message = new UserDataMessageEntity();
        message.setQueueName(USER_DATA_QUEUE);
        message.setData(data);
        message.setQueuesAlreadyReceived(
            List.of(USER_DATA_QUEUE, USER_DATA_ITEM_QUEUE, USER_DATA_WALLET_QUEUE)
        );

        userDataMessageRepository.insertMessageRegistry(message);
    }

    private void sendToItemServiceQueue(String message) throws JsonProcessingException {
        UserCreatedMessageDTO messageConverted = mapper.readValue(message, UserCreatedMessageDTO.class);
        rabbitTemplate.convertAndSend(USER_DATA_ITEM_TOPIC_EXCHANGE, USER_DATA_ITEM_ROUTING_KEY, mapper.writeValueAsString(messageConverted));
    }

    private void sendToWalletServiceQueue(String message) throws JsonProcessingException {
        UserCreatedMessageDTO messageConverted = mapper.readValue(message, UserCreatedMessageDTO.class);
        rabbitTemplate.convertAndSend(USER_DATA_WALLET_TOPIC_EXCHANGE, USER_DATA_WALLET_ROUTING_KEY, mapper.writeValueAsString(messageConverted));
    }

    private String convertBodyAsString(Message messagePayload) {
        byte[] body = messagePayload.getBody();
        return new String(body);
    }
}