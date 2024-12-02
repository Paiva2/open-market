package org.com.openmarket.items.core.domain.repository;

import org.com.openmarket.items.core.domain.entity.ItemAlteration;

public interface ItemAlterationRepository {
    ItemAlteration save(ItemAlteration itemAlteration);
}
