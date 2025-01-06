package org.com.openmarket.items.application.gateway.message;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.com.openmarket.items.application.gateway.message.dto.CommonMessageDTO;
import org.com.openmarket.items.core.domain.usecase.user.disableUser.DisableUserUsecase;
import org.com.openmarket.items.core.domain.usecase.user.insertUser.InsertUserUsecase;
import org.com.openmarket.items.core.domain.usecase.user.insertUser.dto.InsertUserInput;
import org.com.openmarket.items.core.domain.usecase.user.insertUser.exception.UserAlreadyExistsException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

import static org.com.openmarket.items.application.config.constants.QueueConstants.ITEM_QUEUE;

@Slf4j
@Component
@AllArgsConstructor
public class MessageQueue {
    private final ObjectMapper mapper = new ObjectMapper();

    private final InsertUserUsecase insertUserUsecase;
    private final DisableUserUsecase disableUserUsecase;

    @RabbitListener(queues = {ITEM_QUEUE})
    public void receive(@Payload Message messagePayload) {
        try {
            log.info("New message received on queue {}", ITEM_QUEUE);
            String messageBody = convertBodyMessage(messagePayload.getBody());
            CommonMessageDTO messageDTO = mapper.readValue(messageBody, CommonMessageDTO.class);

            switch (messageDTO.getType()) {
                case CREATED -> handleCreated(messageDTO);
                case DELETED -> handleDeleted(messageDTO);
                case UPDATED -> handleUpdated(messageDTO);
                default -> throw new RuntimeException("Event type not recognized!" + messageDTO.getType());
            }
        } catch (UserAlreadyExistsException e) {
            String message = "Error while processing new message";
            log.error(MessageFormat.format("{0}: {1}", message, e));
        } catch (Exception e) {
            String message = "Error while processing new message";
            log.error(MessageFormat.format("{0}: {1}", message, e));
            throw new RuntimeException(message);
        }
    }

    private void handleCreated(CommonMessageDTO messageDTO) throws JsonProcessingException {
        switch (messageDTO.getEvent()) {
            case USER_EVENT -> {
                InsertUserInput input = mapper.readValue(messageDTO.getData(), InsertUserInput.class);
                insertUserUsecase.execute(input);
            }
            default -> throw new RuntimeException("Event not implemented! " + messageDTO.getEvent());
        }
    }

    private void handleUpdated(CommonMessageDTO messageDTO) {
        switch (messageDTO.getEvent()) {
            default -> log.error("Event not implemented! {}", messageDTO.getEvent());
        }
    }

    private void handleDeleted(CommonMessageDTO messageDTO) {
        switch (messageDTO.getEvent()) {
            case USER_EVENT -> disableUserUsecase.execute(Long.valueOf(messageDTO.getData()));
            default -> throw new RuntimeException("Event not implemented! " + messageDTO.getEvent());
        }
    }

    private String convertBodyMessage(byte[] rawBody) {
        return new String(rawBody);
    }
}
