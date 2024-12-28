package org.com.openmarket.market.domain.interfaces;

import org.com.openmarket.market.infra.persistence.entity.PastMessagesEntity;

import java.util.List;

public interface PastMessagesRepository {
    List<PastMessagesEntity> getNonReadMessages();

    List<PastMessagesEntity> saveAll(List<PastMessagesEntity> messages);
}
