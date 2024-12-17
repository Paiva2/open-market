package org.com.openmarket.wallet.application.gateway.controller.messages.walletDataMessages;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.com.openmarket.wallet.application.gateway.controller.messages.walletDataMessages.dto.WalletMessageDTO;
import org.com.openmarket.wallet.application.gateway.controller.messages.walletDataMessages.exception.InvalidMessageFormatException;
import org.com.openmarket.wallet.core.domain.usecase.walletLedger.registerNewTransaction.RegisterNewTransactionUsecase;
import org.com.openmarket.wallet.core.domain.usecase.walletLedger.registerNewTransaction.dto.RegisterNewTransactionInput;
import org.com.openmarket.wallet.core.enums.EnumWalletMessageEvent;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

import static org.com.openmarket.wallet.application.config.constants.QueueConstants.Wallet.WALLET_DATA;

@Slf4j
@Controller
@AllArgsConstructor
public class WalletDataMessagesController {
    private final static ObjectMapper mapper = new ObjectMapper();

    private final RegisterNewTransactionUsecase registerNewTransactionUsecase;

    @Transactional
    @RabbitListener(queues = WALLET_DATA)
    public void execute(@Payload Message message) {
        log.info("Received a new message on {}: {}", WALLET_DATA, message);

        try {
            String bodyConverted = convertBody(message.getBody());
            WalletMessageDTO messageConverted = mapper.readValue(bodyConverted, WalletMessageDTO.class);
            EnumWalletMessageEvent messageEvent = getMessageEvent(messageConverted.getEvent());

            switch (messageEvent) {
                case NEW_TRANSACTION ->
                    registerNewTransactionUsecase.execute(mountRegisterNewTransactionInput(messageConverted));
                default -> throw new RuntimeException("Event not recognized!");
            }
        } catch (InvalidMessageFormatException exception) {
            log.error("Discarded: Error while processing new message on {}, {}", WALLET_DATA, message);
            log.error(exception.getMessage());
        } catch (Exception exception) {
            log.error("Error while processing new message on {}, {}", WALLET_DATA, message);
            log.error(exception.getMessage());
            throw new RuntimeException(exception.getMessage());
        }
    }

    private String convertBody(byte[] body) {
        return new String(body);
    }

    private EnumWalletMessageEvent getMessageEvent(String event) {
        EnumWalletMessageEvent result = null;

        try {
            result = EnumWalletMessageEvent.valueOf(event);
        } catch (Exception exception) {
            log.error("Error while processing event {}, {}", event, exception.getMessage());
        }

        return result;
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
}
