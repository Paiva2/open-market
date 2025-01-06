package org.com.openmarket.customuserstorage.providers.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.com.openmarket.customuserstorage.providers.enumeration.EnumMessageEvent;
import org.com.openmarket.customuserstorage.providers.enumeration.EnumMessageType;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CommonMessageDTO {
    private EnumMessageType type;
    private EnumMessageEvent event;
    private String data;
}