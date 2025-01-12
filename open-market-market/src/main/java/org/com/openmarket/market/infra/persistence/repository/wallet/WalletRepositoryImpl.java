package org.com.openmarket.market.infra.persistence.repository.wallet;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.com.openmarket.market.domain.core.usecase.common.dto.UserWalletViewOutput;
import org.com.openmarket.market.domain.interfaces.WalletRepository;
import org.com.openmarket.market.infra.utils.RestUtils;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class WalletRepositoryImpl implements WalletRepository {
    private final static ObjectMapper mapper = new ObjectMapper();
    private final static String HOST = "http://localhost:8080";
    private final static String URL_PREFIX = "/api/wallet";

    private final RestUtils restUtils;

    @Override
    public UserWalletViewOutput getUserWalletView(String authorizationToken) {
        try {
            String url = HOST.concat(URL_PREFIX).concat("/info");
            String body = restUtils.get(url, authorizationToken);

            return mapper.readValue(body, UserWalletViewOutput.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
