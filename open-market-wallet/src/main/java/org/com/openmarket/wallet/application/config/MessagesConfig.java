package org.com.openmarket.wallet.application.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.com.openmarket.wallet.application.config.dto.MessageDataDTO;
import org.com.openmarket.wallet.core.interfaces.UserDataMessageRepository;
import org.com.openmarket.wallet.infra.persistence.entity.UserDataMessageEntity;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.com.openmarket.wallet.application.config.constants.QueueConstants.UserWallet.*;

@Slf4j
@Component
@AllArgsConstructor
public class MessagesConfig {
    private final static ObjectMapper mapper = new ObjectMapper();

    private final UserDataMessageRepository userDataMessageRepository;
    private final RabbitTemplate rabbitTemplate;

    @PostConstruct
    public void listenNotReadMessages() {
        log.info("Checking for non-read messages!");
        List<UserDataMessageEntity> messages = userDataMessageRepository.getMessagesNotRead();

        if (messages == null || messages.isEmpty()) return;

        try {
            for (UserDataMessageEntity message : messages) {
                List<String> queuesReceives = message.getQueuesAlreadyReceived();
                MessageDataDTO messageData = mapper.readValue(message.getData(), MessageDataDTO.class);

                rabbitTemplate.convertAndSend(USER_DATA_WALLET_TOPIC_EXCHANGE, USER_DATA_WALLET_ROUTING_KEY, messageData);

                queuesReceives.add(USER_DATA_WALLET_QUEUE);
                message.setQueuesAlreadyReceived(queuesReceives);
            }

            userDataMessageRepository.saveAll(messages);
        } catch (Exception e) {
            log.error("Error while trying to fetch non-read messages... {}", e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }
}