package org.com.openmarket.items.core.domain.usecase.itemAlteration.registerItemAlteration;

import lombok.AllArgsConstructor;
import org.com.openmarket.items.core.domain.entity.Item;
import org.com.openmarket.items.core.domain.entity.ItemAlteration;
import org.com.openmarket.items.core.domain.entity.User;
import org.com.openmarket.items.core.domain.enumeration.EnumItemAlteration;
import org.com.openmarket.items.core.domain.repository.ItemAlterationRepository;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RegisterItemAlterationUsecase {
    private final ItemAlterationRepository itemAlterationRepository;

    public void execute(User user, Item item, EnumItemAlteration alteration) {
        ItemAlteration itemAlteration = fillNewItemAlreation(user, item, alteration);
        persistItemAlteration(itemAlteration);
    }

    private ItemAlteration fillNewItemAlreation(User user, Item item, EnumItemAlteration alteration) {
        return ItemAlteration.builder()
            .action(alteration)
            .user(user)
            .item(item)
            .build();
    }

    private void persistItemAlteration(ItemAlteration itemAlteration) {
        itemAlterationRepository.save(itemAlteration);
    }
}
