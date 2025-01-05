package org.com.openmarket.items.core.domain.interfaces.repository;

public interface MessageRepository {
    void sendMessage(String topicExchange, String routingKey, String message);

    void sendMessage(String queueName, String message);
}
