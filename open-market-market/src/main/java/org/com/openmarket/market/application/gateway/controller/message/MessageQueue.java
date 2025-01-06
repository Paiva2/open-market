package org.com.openmarket.market.application.gateway.controller.message;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.com.openmarket.market.domain.core.usecase.category.registerCategory.RegisterCategoryUsecase;
import org.com.openmarket.market.domain.core.usecase.category.registerCategory.dto.RegisterCategoryInput;
import org.com.openmarket.market.domain.core.usecase.category.registerCategory.exception.CategoryAlreadyExistsException;
import org.com.openmarket.market.domain.core.usecase.common.dto.CommonMessageDTO;
import org.com.openmarket.market.domain.core.usecase.user.disableUser.DisableUserUsecase;
import org.com.openmarket.market.domain.core.usecase.user.registerUser.RegisterUserInput;
import org.com.openmarket.market.domain.core.usecase.user.registerUser.RegisterUserUsecase;
import org.com.openmarket.market.domain.core.usecase.user.registerUser.exception.UserAlreadyExistsException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

import static org.com.openmarket.market.application.config.constants.QueueConstants.MARKET_QUEUE;

@Slf4j
@Component
@AllArgsConstructor
public class MessageQueue {
    private final static ObjectMapper mapper = new ObjectMapper();

    private final RegisterCategoryUsecase registerCategoryUsecase;
    private final RegisterUserUsecase registerUserUsecase;
    private final DisableUserUsecase disableUserUsecase;

    @RabbitListener(queues = {MARKET_QUEUE})
    public void receiveMessage(@Payload Message messagePayload) {
        try {
            log.info("New message received on queue {}", MARKET_QUEUE);
            String messageBody = convertBodyMessage(messagePayload.getBody());
            CommonMessageDTO messageDTO = mapper.readValue(messageBody, CommonMessageDTO.class);

            switch (messageDTO.getType()) {
                case CREATED -> handleTypeCreated(messageDTO);
                case UPDATED -> handleTypeUpdated(messageDTO);
                case DELETED -> handleTypeDeleted(messageDTO);
                default -> throw new RuntimeException("Event type not recognized!" + messageDTO.getEvent());
            }
        } catch (UserAlreadyExistsException | CategoryAlreadyExistsException e) {
            String message = "Error while processing new message";
            log.error(MessageFormat.format("{0}: {1}", message, e));
        } catch (Exception e) {
            String message = "Error while processing new message";
            log.error(MessageFormat.format("{0}: {1}", message, e));
            throw new RuntimeException(message);
        }
    }

    private void handleTypeCreated(CommonMessageDTO messageDTO) throws JsonProcessingException {
        switch (messageDTO.getEvent()) {
            case CATEGORY_EVENT -> {
                RegisterCategoryInput input = new RegisterCategoryInput(messageDTO.getData());
                registerCategoryUsecase.execute(input);
            }
            case USER_EVENT -> {
                RegisterUserInput input = mapper.readValue(messageDTO.getData(), RegisterUserInput.class);
                registerUserUsecase.execute(input);
            }
            default -> log.error("Event not recognized! {}", messageDTO.getEvent());
        }
    }

    private void handleTypeUpdated(CommonMessageDTO messageDTO) {
        switch (messageDTO.getEvent()) {
            default -> log.error("Event not recognized! {}", messageDTO.getEvent());
        }
    }

    private void handleTypeDeleted(CommonMessageDTO messageDTO) {
        switch (messageDTO.getEvent()) {
            case USER_EVENT -> disableUserUsecase.execute(messageDTO.getData());
            default -> log.error("Event not recognized! {}", messageDTO.getEvent());
        }
    }

    private String convertBodyMessage(byte[] rawBody) {
        return new String(rawBody);
    }
}
