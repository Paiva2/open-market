package org.com.openmarket.market.application.gateway.controller.message.userData;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.com.openmarket.market.application.config.dto.MessageDataDTO;
import org.com.openmarket.market.domain.core.usecase.user.disableUser.DisableUserUsecase;
import org.com.openmarket.market.domain.core.usecase.user.registerUser.RegisterUserInput;
import org.com.openmarket.market.domain.core.usecase.user.registerUser.RegisterUserUsecase;
import org.com.openmarket.market.domain.enumeration.EnumUserDataMessageEvent;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

import static org.com.openmarket.market.application.config.constants.QueueConstants.MarketUserData.USER_DATA_MARKET_QUEUE;

@Slf4j
@Controller
@AllArgsConstructor
public class UserDataMessagesController {
    private final static ObjectMapper mapper = new ObjectMapper();

    private final RegisterUserUsecase registerUserUsecase;
    private final DisableUserUsecase disableUserUsecase;

    @RabbitListener(queues = {USER_DATA_MARKET_QUEUE})
    public void execute(@Payload Message messagePayload) {
        try {
            String bodyString = convertBody(messagePayload);
            MessageDataDTO message = mapper.readValue(bodyString, MessageDataDTO.class);
            EnumUserDataMessageEvent event = convertEvent(message.getEvent());

            switch (event) {
                case CREATED ->
                    registerUserUsecase.execute(RegisterUserInput.toInput(message.getExtId(), message.getUsername(), message.getEmail()));
                case DELETED -> disableUserUsecase.execute(message.getExtId());
                default -> throw new RuntimeException("Event not implemented yet!");
            }
        } catch (Exception e) {
            String message = "Error while reading " + USER_DATA_MARKET_QUEUE;
            log.error(message, e);
            throw new RuntimeException(message);
        }
    }

    private String convertBody(Message messagePayload) {
        return new String(messagePayload.getBody());
    }

    private EnumUserDataMessageEvent convertEvent(String messageEvent) {
        try {
            return EnumUserDataMessageEvent.valueOf(messageEvent);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
