package org.com.openmarket.items.application.gateway.message.userQueue;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.com.openmarket.items.application.gateway.message.userQueue.dto.UserDataMessageInput;
import org.com.openmarket.items.core.domain.usecase.user.insertUser.InsertUserUsecase;
import org.com.openmarket.items.core.domain.usecase.user.insertUser.dto.InsertUserInput;
import org.com.openmarket.items.core.domain.usecase.user.insertUser.exception.UserAlreadyExistsException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import static org.com.openmarket.items.application.config.constants.QueueConstants.UserItem.USER_DATA_ITEM_QUEUE;

//todo: DLQ
@Slf4j
@Component
@AllArgsConstructor
public class UserDataMessageQueue {
    private final ObjectMapper mapper = new ObjectMapper();

    private final InsertUserUsecase insertUserUsecase;

    @RabbitListener(queues = {USER_DATA_ITEM_QUEUE})
    public void receive(@Payload Message messagePayload) {
        try {
            log.info("New message received on queue {}", USER_DATA_ITEM_QUEUE);
            String messageBody = convertBodyMessage(messagePayload.getBody());
            UserDataMessageInput input = mapper.readValue(messageBody, UserDataMessageInput.class);

            switch (input.getEvent()) {
                case CREATED -> insertUserUsecase.execute(InsertUserInput.toUsecase(input));
                case UPDATED -> throw new RuntimeException("Event not implemented!" + input.getEvent());
                case DELETED -> throw new RuntimeException("Event not implemented!" + input.getEvent());
                default -> throw new RuntimeException("Event not recognized!" + input.getEvent());
            }
        } catch (UserAlreadyExistsException e) {
            String message = "Error while processing new message";
            log.error("{}: {}", message, e.getMessage());
        } catch (Exception e) {
            String message = "Error while processing new message";
            log.error("{}: {}", message, e);
            throw new RuntimeException(message);
        }
    }

    private String convertBodyMessage(byte[] rawBody) {
        return new String(rawBody);
    }
}
