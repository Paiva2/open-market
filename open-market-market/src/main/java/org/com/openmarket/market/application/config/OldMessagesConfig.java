package org.com.openmarket.market.application.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.com.openmarket.market.domain.core.usecase.common.dto.CommonMessageDTO;
import org.com.openmarket.market.domain.interfaces.PastMessagesRepository;
import org.com.openmarket.market.infra.persistence.entity.PastMessagesEntity;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Configuration;

import java.util.List;

import static org.com.openmarket.market.application.config.constants.QueueConstants.MARKET_QUEUE;

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
                CommonMessageDTO messageData = mapper.readValue(message.getData(), CommonMessageDTO.class);

                rabbitTemplate.convertAndSend(MARKET_QUEUE, mapper.writeValueAsString(messageData));

                queuesReceives.add(MARKET_QUEUE);
                message.setQueuesAlreadyReceived(queuesReceives);
            }

            pastMessagesRepository.saveAll(messages);
        } catch (Exception e) {
            log.error("Error while trying to fetch non-read messages... {}", e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }
}