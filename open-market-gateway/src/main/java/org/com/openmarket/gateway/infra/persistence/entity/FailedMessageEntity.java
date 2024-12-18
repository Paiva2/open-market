package org.com.openmarket.gateway.infra.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "failed-messages")
public class FailedMessageEntity {
    @Id
    private String id;

    private String message;

    private String queueName;

    private String routingKey;

    private String exchange;

    private Integer requeueQuantity;

    private String createdAt;
}
