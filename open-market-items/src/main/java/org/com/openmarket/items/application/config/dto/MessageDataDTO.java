package org.com.openmarket.items.application.config.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageDataDTO {
    private String event;
    private Long extId;
    private String username;
    private String email;
}
