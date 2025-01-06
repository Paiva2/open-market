package org.com.openmarket.items.application.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.com.openmarket.items.application.gateway.message.dto.CommonMessageDTO;
import org.com.openmarket.items.core.domain.interfaces.repository.UserDataMessageRepository;
import org.com.openmarket.items.infra.persistence.entity.PastMessagesEntity;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.com.openmarket.items.application.config.constants.QueueConstants.ITEM_QUEUE;

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
        List<PastMessagesEntity> messages = userDataMessageRepository.getMessagesNotRead();

        try {
            for (PastMessagesEntity message : messages) {
                List<String> queuesReceives = message.getQueuesAlreadyReceived();
                CommonMessageDTO messageData = mapper.readValue(message.getData(), CommonMessageDTO.class);

                rabbitTemplate.convertAndSend(ITEM_QUEUE, mapper.writeValueAsString(messageData));

                queuesReceives.add(ITEM_QUEUE);
                message.setQueuesAlreadyReceived(queuesReceives);
            }

            userDataMessageRepository.saveAll(messages);
        } catch (Exception e) {
            log.error("Error while trying to fetch non-read messages... {}", e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }
}