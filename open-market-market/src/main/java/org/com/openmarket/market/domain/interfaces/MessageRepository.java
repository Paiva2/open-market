package org.com.openmarket.market.domain.interfaces;

public interface MessageRepository {
    void sendMessage(String queueName, String data);
}
