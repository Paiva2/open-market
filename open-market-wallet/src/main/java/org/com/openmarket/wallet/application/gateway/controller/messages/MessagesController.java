package org.com.openmarket.wallet.application.gateway.controller.messages;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.com.openmarket.wallet.application.gateway.controller.messages.dto.UserMessageDTO;
import org.com.openmarket.wallet.core.domain.usecase.user.registerUser.RegisterUserUsecase;
import org.com.openmarket.wallet.core.domain.usecase.user.registerUser.dto.RegisterUserInput;
import org.com.openmarket.wallet.core.domain.usecase.user.registerUser.exception.UserAlreadyExistsException;
import org.com.openmarket.wallet.core.enums.EnumUserMessageEvent;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

import static org.com.openmarket.wallet.application.config.constants.QueueConstants.UserWallet.USER_DATA_WALLET_QUEUE;

@Slf4j
@Controller
@AllArgsConstructor
public class MessagesController {
    private final static ObjectMapper mapper = new ObjectMapper();

    private final RegisterUserUsecase registerUserUsecase;

    @Transactional
    @RabbitListener(queues = USER_DATA_WALLET_QUEUE)
    public void execute(@Payload Message message) {
        log.info("Received a new message: {}", message);

        try {
            String bodyConverted = convertBody(message.getBody());
            UserMessageDTO messageConverted = mapper.readValue(bodyConverted, UserMessageDTO.class);
            EnumUserMessageEvent messageEvent = getMessageEvent(messageConverted.getEvent());

            switch (messageEvent) {
                case CREATED -> registerUserUsecase.execute(mountRegisterUserInput(messageConverted));
                case UPDATED, DELETED -> throw new RuntimeException("Event not implemented");
                default -> throw new RuntimeException("Event not recognized!");
            }
        } catch (UserAlreadyExistsException exception) {
            log.warn("Message discarded, reason: {}", exception.getMessage());
        } catch (Exception exception) {
            log.error("Error while processing new message: {}", message);
            throw new RuntimeException(exception.getMessage());
        }
    }

    private String convertBody(byte[] body) {
        return new String(body);
    }

    private EnumUserMessageEvent getMessageEvent(String eventReceived) {
        EnumUserMessageEvent messageEvent;

        try {
            messageEvent = EnumUserMessageEvent.valueOf(eventReceived);
        } catch (Exception e) {
            messageEvent = null;
        }

        return messageEvent;
    }

    private RegisterUserInput mountRegisterUserInput(UserMessageDTO message) {
        return RegisterUserInput.builder()
            .externalId(message.getExtId())
            .email(message.getEmail())
            .userName(message.getUsername())
            .build();
    }
}
