package org.com.openmarket.items.core.domain.usecase.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.com.openmarket.items.core.domain.enumeration.EnumMessageEvent;
import org.com.openmarket.items.core.domain.enumeration.EnumMessageType;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommonMessageDTO {
    private EnumMessageType type;
    private EnumMessageEvent event;
    private String data;
}
