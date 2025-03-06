package org.com.openmarket.market.infra.persistence.repository.wallet;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.com.openmarket.market.domain.core.usecase.common.dto.UserWalletViewOutput;
import org.com.openmarket.market.domain.interfaces.WalletRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Component
@AllArgsConstructor
public class WalletRepositoryImpl implements WalletRepository {
    private final static ObjectMapper mapper = new ObjectMapper();
    private final static String HOST = "http://localhost:8080";
    private final static String URL_PREFIX = "/api/wallet";

    private final WebClient webClient;

    @Override
    public UserWalletViewOutput getUserWalletView() {
        try {
            String url = HOST.concat(URL_PREFIX).concat("/info");
            
            String body = webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(String.class)
                .block();

            return mapper.readValue(body, UserWalletViewOutput.class);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException("Error while fetching user wallet...");
        }
    }
}
