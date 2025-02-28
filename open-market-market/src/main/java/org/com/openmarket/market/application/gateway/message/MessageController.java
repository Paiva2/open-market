package org.com.openmarket.market.application.gateway.message;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.com.openmarket.market.domain.core.usecase.common.dto.CommonMessageDTO;
import org.com.openmarket.market.domain.core.usecase.common.exception.ItemNotFoundException;
import org.com.openmarket.market.domain.core.usecase.common.exception.UserDisabledException;
import org.com.openmarket.market.domain.core.usecase.common.exception.UserNotFoundException;
import org.com.openmarket.market.domain.core.usecase.userItem.CreateUserItemInput;
import org.com.openmarket.market.domain.core.usecase.userItem.CreateUserItemUsecase;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.RestController;

import java.text.MessageFormat;

import static org.com.openmarket.market.application.config.constants.QueueConstants.MARKET_QUEUE;

@Slf4j
@RestController
@AllArgsConstructor
public class MessageController {
    private final ObjectMapper mapper = new ObjectMapper();

    private final CreateUserItemUsecase createUserItemUsecase;

    @RabbitListener(queues = {MARKET_QUEUE})
    public void execute(@Payload Message messagePayload) {
        try {
            log.info("New message received on queue {}", MARKET_QUEUE);
            String messageBody = convertBodyMessage(messagePayload.getBody());
            CommonMessageDTO messageDTO = mapper.readValue(messageBody, CommonMessageDTO.class);

            switch (messageDTO.getType()) {
                case CREATED -> handleCreated(messageDTO);
                case DELETED -> handleDeleted(messageDTO);
                case UPDATED -> handleUpdated(messageDTO);
                default -> throw new RuntimeException("Event type not recognized!" + messageDTO.getType());
            }
        } catch (ItemNotFoundException | UserDisabledException | UserNotFoundException e) {
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
            case USER_ITEM_EVENT -> {
                CreateUserItemInput input = mapper.readValue(messageDTO.getData(), CreateUserItemInput.class);
                createUserItemUsecase.execute(input);
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
            default -> throw new RuntimeException("Event not implemented! " + messageDTO.getEvent());
        }
    }

    private String convertBodyMessage(byte[] rawBody) {
        return new String(rawBody);
    }
}
