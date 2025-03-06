package org.com.openmarket.market.application.gateway.controller.message;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.com.openmarket.market.domain.core.usecase.category.deleteCategory.DeleteCategoryUsecase;
import org.com.openmarket.market.domain.core.usecase.category.deleteCategory.dto.DeleteCategoryInput;
import org.com.openmarket.market.domain.core.usecase.category.registerCategory.RegisterCategoryUsecase;
import org.com.openmarket.market.domain.core.usecase.category.registerCategory.dto.RegisterCategoryInput;
import org.com.openmarket.market.domain.core.usecase.category.registerCategory.exception.CategoryAlreadyExistsException;
import org.com.openmarket.market.domain.core.usecase.common.dto.CommonMessageDTO;
import org.com.openmarket.market.domain.core.usecase.common.exception.CategoryNotFoundException;
import org.com.openmarket.market.domain.core.usecase.common.exception.ItemNotActiveException;
import org.com.openmarket.market.domain.core.usecase.item.createItem.CreateItemUsecase;
import org.com.openmarket.market.domain.core.usecase.item.createItem.dto.CreateItemInput;
import org.com.openmarket.market.domain.core.usecase.item.updateItem.UpdateItemUsecase;
import org.com.openmarket.market.domain.core.usecase.item.updateItem.dto.UpdateItemInput;
import org.com.openmarket.market.domain.core.usecase.user.disableUser.DisableUserUsecase;
import org.com.openmarket.market.domain.core.usecase.user.registerUser.RegisterUserInput;
import org.com.openmarket.market.domain.core.usecase.user.registerUser.RegisterUserUsecase;
import org.com.openmarket.market.domain.core.usecase.user.registerUser.exception.UserAlreadyExistsException;
import org.com.openmarket.market.domain.core.usecase.userItem.createUserItem.CreateUserItemInput;
import org.com.openmarket.market.domain.core.usecase.userItem.createUserItem.CreateUserItemUsecase;
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
    private final DeleteCategoryUsecase deleteCategoryUsecase;

    private final RegisterUserUsecase registerUserUsecase;
    private final DisableUserUsecase disableUserUsecase;

    private final CreateItemUsecase createItemUsecase;
    private final UpdateItemUsecase updateItemUsecase;

    private final CreateUserItemUsecase createUserItemUsecase;

    @Transactional
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
        } catch (UserAlreadyExistsException | CategoryAlreadyExistsException | ItemNotActiveException |
                 CategoryNotFoundException e) {
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
                RegisterCategoryInput input = mapper.readValue(messageDTO.getData(), RegisterCategoryInput.class);
                registerCategoryUsecase.execute(input);
            }
            case USER_EVENT -> {
                RegisterUserInput input = mapper.readValue(messageDTO.getData(), RegisterUserInput.class);
                registerUserUsecase.execute(input);
            }
            case ITEM_EVENT -> {
                CreateItemInput input = mapper.readValue(messageDTO.getData(), CreateItemInput.class);
                createItemUsecase.execute(input);
            }
            case USER_ITEM_EVENT -> {
                CreateUserItemInput input = mapper.readValue(messageDTO.getData(), CreateUserItemInput.class);
                createUserItemUsecase.execute(input);
            }
            default -> log.error("Event not recognized! {}", messageDTO.getEvent());
        }
    }

    private void handleTypeUpdated(CommonMessageDTO messageDTO) throws JsonProcessingException {
        switch (messageDTO.getEvent()) {
            case ITEM_EVENT -> {
                UpdateItemInput input = mapper.readValue(messageDTO.getData(), UpdateItemInput.class);
                updateItemUsecase.execute(input);
            }
            default -> log.error("Event not recognized! {}", messageDTO.getEvent());
        }
    }

    private void handleTypeDeleted(CommonMessageDTO messageDTO) throws JsonProcessingException {
        switch (messageDTO.getEvent()) {
            case USER_EVENT -> disableUserUsecase.execute(messageDTO.getData());
            case CATEGORY_EVENT -> {
                DeleteCategoryInput input = mapper.readValue(messageDTO.getData(), DeleteCategoryInput.class);
                deleteCategoryUsecase.execute(input);
            }
            default -> log.error("Event not recognized! {}", messageDTO.getEvent());
        }
    }

    private String convertBodyMessage(byte[] rawBody) {
        return new String(rawBody);
    }
}
