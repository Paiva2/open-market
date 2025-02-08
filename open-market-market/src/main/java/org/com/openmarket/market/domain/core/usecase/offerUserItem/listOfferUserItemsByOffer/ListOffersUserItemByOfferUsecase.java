package org.com.openmarket.market.domain.core.usecase.offerUserItem.listOfferUserItemsByOffer;

import lombok.AllArgsConstructor;
import org.com.openmarket.market.domain.core.entity.Offer;
import org.com.openmarket.market.domain.core.entity.OfferUserItem;
import org.com.openmarket.market.domain.core.entity.User;
import org.com.openmarket.market.domain.core.usecase.common.exception.OfferNotFoundException;
import org.com.openmarket.market.domain.core.usecase.common.exception.UserDisabledException;
import org.com.openmarket.market.domain.core.usecase.common.exception.UserNotFoundException;
import org.com.openmarket.market.domain.core.usecase.offerUserItem.listOfferUserItemsByOffer.dto.ListOffersUserItemByOfferOutput;
import org.com.openmarket.market.domain.interfaces.OfferRepository;
import org.com.openmarket.market.domain.interfaces.OfferUserItemRepository;
import org.com.openmarket.market.domain.interfaces.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ListOffersUserItemByOfferUsecase {
    private final UserRepository userRepository;
    private final OfferRepository offerRepository;
    private final OfferUserItemRepository offerUserItemRepository;

    public List<ListOffersUserItemByOfferOutput> execute(String externalUserId, UUID offerId) {
        User user = findUser(externalUserId);

        if (!user.getEnabled()) {
            throw new UserDisabledException();
        }

        Offer offer = findOffer(offerId);

        List<OfferUserItem> offerUserItems = findOfferUserItems(offer);

        return offerUserItems.stream().map(ListOffersUserItemByOfferOutput::new).toList();
    }

    private User findUser(String externalUserId) {
        return userRepository.findByExternalId(externalUserId).orElseThrow(UserNotFoundException::new);
    }

    private Offer findOffer(UUID offerId) {
        return offerRepository.findById(offerId).orElseThrow(OfferNotFoundException::new);
    }

    private List<OfferUserItem> findOfferUserItems(Offer offer) {
        return offerUserItemRepository.findByOfferIdWithDeps(offer.getId());
    }
}
