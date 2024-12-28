package org.com.openmarket.market.application.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.com.openmarket.market.application.config.dto.MessageDataDTO;
import org.com.openmarket.market.domain.interfaces.PastMessagesRepository;
import org.com.openmarket.market.infra.persistence.entity.PastMessagesEntity;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Configuration;

import java.util.List;

import static org.com.openmarket.market.application.config.constants.QueueConstants.MarketUserData.*;

@Slf4j
@Configuration
@AllArgsConstructor
public class OldMessagesConfig {
    private static final ObjectMapper mapper = new ObjectMapper();

    private final PastMessagesRepository pastMessagesRepository;
    private final RabbitTemplate rabbitTemplate;

    @PostConstruct
    public void execute() {
        List<PastMessagesEntity> messages = pastMessagesRepository.getNonReadMessages();

        if (messages == null || messages.isEmpty()) return;

        try {
            for (PastMessagesEntity message : messages) {
                List<String> queuesReceives = message.getQueuesAlreadyReceived();
                MessageDataDTO messageData = mapper.readValue(message.getData(), MessageDataDTO.class);

                rabbitTemplate.convertAndSend(USER_DATA_MARKET_TOPIC_EXCHANGE, USER_DATA_MARKET_ROUTING_KEY, messageData);

                queuesReceives.add(USER_DATA_MARKET_QUEUE);
                message.setQueuesAlreadyReceived(queuesReceives);
            }

            pastMessagesRepository.saveAll(messages);
        } catch (Exception e) {
            log.error("Error while trying to fetch non-read messages... {}", e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }
}