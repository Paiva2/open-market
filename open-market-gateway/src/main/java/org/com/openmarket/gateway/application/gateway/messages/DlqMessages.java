package org.com.openmarket.gateway.application.gateway.messages;

import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.com.openmarket.gateway.core.domain.entity.FailedMessage;
import org.com.openmarket.gateway.core.interfaces.FailedMessageRepository;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static org.com.openmarket.gateway.application.config.dlq.constants.QueueConstants.Dlq.DEAD_LETTER_QUEUE;

@Slf4j
@Component
@AllArgsConstructor
public class DlqMessages {
    private final static Gson gson = new Gson();
    private final static int MAX_RETRIES = 3;
    private final static String REQUEUE_RETRIES_HEADER = "x-message-requeue-retries";

    private final RabbitTemplate rabbitTemplate;
    private final FailedMessageRepository failedMessageRepository;

    @RabbitListener(queues = {DEAD_LETTER_QUEUE})
    public void execute(@Payload Message messagePayload) {
        Integer maxRetries = messagePayload.getMessageProperties().getHeader(REQUEUE_RETRIES_HEADER);

        List<Object> messageXDeath = messagePayload.getMessageProperties().getHeader("x-death");
        HashMap<String, Object> messageSourceData = (HashMap) messageXDeath.get(0);
        String sourceQueue = (String) messageSourceData.get("queue");
        String sourceExchange = (String) messageSourceData.get("exchange");
        String sourceRoutingKey = ((List<String>) messageSourceData.get("routing-keys")).get(0);

        if (sourceExchange == null || sourceRoutingKey == null) {
            log.info("Message discarded because source exchange is null: {}", messagePayload);
            return;
        }

        if (maxRetries == null) {
            messagePayload.getMessageProperties().setHeader(REQUEUE_RETRIES_HEADER, 1);
            rabbitTemplate.send(sourceExchange, sourceRoutingKey, messagePayload);
            log.info("Message requeued: {}", messagePayload);
            return;
        }

        if (maxRetries >= MAX_RETRIES) {
            log.info("Message discarded for reaching max retries: {}", messagePayload);

            try {
                failedMessageRepository.save(new FailedMessage(
                    gson.toJson(messagePayload),
                    sourceQueue,
                    sourceRoutingKey,
                    sourceExchange,
                    0,
                    new Date().toString()
                ));
            } catch (Exception exception) {
                log.error("Error while saving failed message", exception);
                throw new RuntimeException(exception);
            }

            return;
        }

        messagePayload.getMessageProperties().setHeader(REQUEUE_RETRIES_HEADER, maxRetries + 1);
        rabbitTemplate.send(sourceExchange, sourceRoutingKey, messagePayload);
        log.info("Message requeued: {}", messagePayload);
    }
}