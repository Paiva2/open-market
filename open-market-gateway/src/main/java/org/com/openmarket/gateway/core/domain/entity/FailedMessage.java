package org.com.openmarket.gateway.core.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FailedMessage {
    private String id;
    private String message;
    private String queueName;
    private String routingKey;
    private String exchange;
    private Integer requeueQuantity;
    private String createdAt;

    public FailedMessage(String message, String queueName, String routingKey, String exchange, Integer requeueQuantity, String createdAt) {
        this.message = message;
        this.queueName = queueName;
        this.routingKey = routingKey;
        this.exchange = exchange;
        this.requeueQuantity = requeueQuantity;
        this.createdAt = createdAt;
    }

    public FailedMessage(String message, String queueName, Integer requeueQuantity, String createdAt) {
        this.message = message;
        this.queueName = queueName;
        this.requeueQuantity = requeueQuantity;
        this.createdAt = createdAt;
    }
}
