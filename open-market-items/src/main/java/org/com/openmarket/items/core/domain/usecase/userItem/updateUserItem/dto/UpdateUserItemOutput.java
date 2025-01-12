package org.com.openmarket.items.core.domain.usecase.userItem.updateUserItem.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.com.openmarket.items.core.domain.entity.UserItem;

import java.util.Date;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserItemOutput {
    private String externalUserId;
    private UUID itemId;
    private Long quantity;
    private Date createdAt;
    private Date updatedAt;

    public static UpdateUserItemOutput toOutput(UserItem userItem) {
        return UpdateUserItemOutput.builder()
            .externalUserId(userItem.getUser().getExternalId())
            .itemId(userItem.getItem().getId())
            .quantity(userItem.getQuantity())
            .createdAt(userItem.getCreatedAt())
            .updatedAt(userItem.getUpdatedAt())
            .build();
    }
}
