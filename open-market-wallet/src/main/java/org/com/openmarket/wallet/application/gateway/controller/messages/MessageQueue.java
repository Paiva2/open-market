package org.com.openmarket.wallet.application.gateway.controller.messages;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.com.openmarket.wallet.application.gateway.controller.messages.dto.CommonMessageDTO;
import org.com.openmarket.wallet.application.gateway.controller.messages.dto.WalletMessageDTO;
import org.com.openmarket.wallet.application.gateway.controller.messages.exception.InvalidMessageFormatException;
import org.com.openmarket.wallet.core.domain.usecase.user.disableUser.DisableUserUsecase;
import org.com.openmarket.wallet.core.domain.usecase.user.registerUser.RegisterUserUsecase;
import org.com.openmarket.wallet.core.domain.usecase.user.registerUser.dto.RegisterUserInput;
import org.com.openmarket.wallet.core.domain.usecase.walletLedger.registerNewTransaction.RegisterNewTransactionUsecase;
import org.com.openmarket.wallet.core.domain.usecase.walletLedger.registerNewTransaction.dto.RegisterNewTransactionInput;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

import java.text.MessageFormat;

import static org.com.openmarket.wallet.application.config.constants.QueueConstants.WALLET_QUEUE;

@Slf4j
@Controller
@AllArgsConstructor
public class MessageQueue {
    private final static ObjectMapper mapper = new ObjectMapper();

    private final RegisterUserUsecase registerUserUsecase;
    private final DisableUserUsecase disableUserUsecase;
    private final RegisterNewTransactionUsecase registerNewTransactionUsecase;

    @Transactional
    @RabbitListener(queues = WALLET_QUEUE)
    public void execute(@Payload Message message) {
        log.info("Received a new message on {}: {}", WALLET_QUEUE, message);

        try {
            String bodyConverted = convertBody(message.getBody());
            CommonMessageDTO commonMessageDTO = mapper.readValue(bodyConverted, CommonMessageDTO.class);

            switch (commonMessageDTO.getType()) {
                case NEW_TRANSACTION -> {
                    WalletMessageDTO messageConverted = mapper.readValue(bodyConverted, WalletMessageDTO.class);
                    registerNewTransactionUsecase.execute(mountRegisterNewTransactionInput(messageConverted));
                }
                case CREATED -> handleCreated(commonMessageDTO);
                case UPDATED -> handleUpdated(commonMessageDTO);
                case DELETED -> handleDeleted(commonMessageDTO);
                default -> throw new RuntimeException("Event not recognized!");
            }
        } catch (InvalidMessageFormatException exception) {
            log.error(MessageFormat.format("Discarded: Error while processing new message on {0}, {1}", WALLET_QUEUE, message));
            log.error(exception.getMessage());
        } catch (Exception exception) {
            log.error(MessageFormat.format("Error while processing new message on {0}, {1}", WALLET_QUEUE, message));
            log.error(exception.getMessage());
            throw new RuntimeException(exception.getMessage());
        }
    }

    private String convertBody(byte[] body) {
        return new String(body);
    }

    private RegisterNewTransactionInput mountRegisterNewTransactionInput(WalletMessageDTO messageConverted) {
        if (messageConverted.getTransaction() == null) {
            throw new InvalidMessageFormatException("Invalid message. Transaction is null!");
        }

        return RegisterNewTransactionInput.builder()
            .externalUserId(messageConverted.getExternalUserId())
            .targetWalletId(messageConverted.getTransaction().getTargetWalletId())
            .description(messageConverted.getTransaction().getDescription())
            .value(messageConverted.getTransaction().getValue())
            .type(messageConverted.getTransaction().getType())
            .build();
    }

    private void handleCreated(CommonMessageDTO commonMessageDTO) throws JsonProcessingException {
        switch (commonMessageDTO.getEvent()) {
            case USER_EVENT -> {
                RegisterUserInput input = mapper.readValue(commonMessageDTO.getData(), RegisterUserInput.class);
                registerUserUsecase.execute(input);
            }
            default -> log.error("Event not implemented {}", commonMessageDTO.getEvent());
        }
    }

    private void handleUpdated(CommonMessageDTO commonMessageDTO) {
        switch (commonMessageDTO.getEvent()) {
            default -> log.error("Event not implemented {}", commonMessageDTO.getEvent());
        }
    }

    private void handleDeleted(CommonMessageDTO commonMessageDTO) {
        switch (commonMessageDTO.getEvent()) {
            case USER_EVENT -> {
                disableUserUsecase.execute(commonMessageDTO.getData());
            }
            default -> log.error("Event not implemented {}", commonMessageDTO.getEvent());
        }
    }
}
