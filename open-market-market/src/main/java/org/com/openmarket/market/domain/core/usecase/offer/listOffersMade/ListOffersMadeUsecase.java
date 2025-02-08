package org.com.openmarket.market.domain.core.usecase.offer.listOffersMade;

import lombok.AllArgsConstructor;
import org.com.openmarket.market.domain.core.entity.Offer;
import org.com.openmarket.market.domain.core.entity.User;
import org.com.openmarket.market.domain.core.usecase.common.dto.PageableList;
import org.com.openmarket.market.domain.core.usecase.common.exception.UserDisabledException;
import org.com.openmarket.market.domain.core.usecase.common.exception.UserNotFoundException;
import org.com.openmarket.market.domain.core.usecase.offer.listOffersMade.dto.ListOffersMadeOutput;
import org.com.openmarket.market.domain.interfaces.OfferRepository;
import org.com.openmarket.market.domain.interfaces.UserRepository;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ListOffersMadeUsecase {
    private final UserRepository userRepository;
    private final OfferRepository offerRepository;

    public PageableList<ListOffersMadeOutput> execute(String externalUserId, int page, int size) {
        User user = findUser(externalUserId);

        if (!user.getEnabled()) {
            throw new UserDisabledException();
        }

        if (page < 1) {
            page = 5;
        }

        if (size < 5) {
            size = 5;
        } else if (size > 50) {
            size = 50;
        }

        PageableList<Offer> offers = findOffersByUser(user, page, size);

        return new PageableList<>(
            offers.getPage(),
            offers.getSize(),
            offers.getTotalItems(),
            offers.getTotalPages(),
            offers.isLast(),
            offers.getData().stream().map(ListOffersMadeOutput::new).toList()
        );
    }

    private User findUser(String externalUserId) {
        return userRepository.findByExternalId(externalUserId).orElseThrow(UserNotFoundException::new);
    }

    private PageableList<Offer> findOffersByUser(User user, int page, int size) {
        return offerRepository.findAllByUser(user.getId(), page, size);
    }
}
