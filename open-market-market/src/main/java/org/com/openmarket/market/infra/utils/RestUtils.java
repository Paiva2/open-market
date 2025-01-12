package org.com.openmarket.market.infra.utils;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@AllArgsConstructor
public class RestUtils {
    private final static int TOTAL_TIMEOUT_MILIS = 25000; //25sec
    private final RestTemplate restTemplate = new RestTemplate();

    @PostConstruct
    public void init() {
        restTemplate.setRequestFactory(getClientHttpRequestFactory());
    }

    private ClientHttpRequestFactory getClientHttpRequestFactory() {
        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        clientHttpRequestFactory.setConnectTimeout(TOTAL_TIMEOUT_MILIS);

        return clientHttpRequestFactory;
    }

    public String get(String url, String authorizationToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + authorizationToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        return response.getBody();
    }
}
