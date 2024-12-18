package org.com.openmarket.gateway.infra.persistence.mapper;

import org.com.openmarket.gateway.core.domain.entity.FailedMessage;
import org.com.openmarket.gateway.infra.persistence.entity.FailedMessageEntity;
import org.springframework.beans.BeanUtils;

public class FailedMessageMapper {
    public static FailedMessage toDomain(FailedMessageEntity persistenceEntity) {
        if (persistenceEntity == null) return null;
        FailedMessage failedMessage = new FailedMessage();
        copyProperties(persistenceEntity, failedMessage);

        return failedMessage;
    }

    public static FailedMessageEntity toPersistence(FailedMessage domainEntity) {
        if (domainEntity == null) return null;
        FailedMessageEntity failedMessageEntity = new FailedMessageEntity();
        copyProperties(domainEntity, failedMessageEntity);

        return failedMessageEntity;
    }

    public static void copyProperties(Object source, Object target) {
        BeanUtils.copyProperties(source, target);
    }
}
