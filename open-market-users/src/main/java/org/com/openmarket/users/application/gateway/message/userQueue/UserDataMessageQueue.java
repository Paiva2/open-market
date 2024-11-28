package org.com.openmarket.users.application.gateway.message.userQueue;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import static org.com.openmarket.users.application.config.constants.QueueConstants.User.USER_DATA_QUEUE;

//todo: make DLQ
@Slf4j
@Component
public class UserDataMessageQueue {
    @RabbitListener(queues = {USER_DATA_QUEUE})
    public void execute(@Payload Message messagePayload) {
        try {
            log.info("New message received on queue {}", USER_DATA_QUEUE);
            String bodyConverted = convertBodyAsString(messagePayload);
            // send to another queues
        } catch (Exception e) {
            String message = "Error while receiving a new message";
            log.error(message, e);
            throw new RuntimeException(message);
        }
    }

    private String convertBodyAsString(Message messagePayload) {
        byte[] body = messagePayload.getBody();
        return new String(body);
    }
}