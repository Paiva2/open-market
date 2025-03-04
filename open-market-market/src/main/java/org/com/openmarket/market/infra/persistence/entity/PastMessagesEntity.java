package org.com.openmarket.market.infra.persistence.entity;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@NoArgsConstructor
@Document(collection = "past-messages")
@AllArgsConstructor
public class PastMessagesEntity {
    @Id
    private String id;

    private String data;

    private String createdAt;

    private String queueName;

    private List<String> queuesAlreadyReceived;
}
