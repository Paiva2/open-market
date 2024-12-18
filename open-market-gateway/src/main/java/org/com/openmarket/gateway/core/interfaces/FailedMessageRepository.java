package org.com.openmarket.gateway.core.interfaces;

import org.com.openmarket.gateway.core.domain.entity.FailedMessage;

public interface FailedMessageRepository {
    FailedMessage save(FailedMessage failedMessage);
}
