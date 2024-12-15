package org.com.openmarket.items.infra.persistence.entity;

import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@NoArgsConstructor
@Document(collection = "messages-user-data")
public class UserDataMessageEntity {
    @Id
    private String id;

    private String queueName;

    private String data;

    private String createdAt;

    private List<String> queuesAlreadyReceived;
}