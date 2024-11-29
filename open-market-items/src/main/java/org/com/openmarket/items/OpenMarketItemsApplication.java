package org.com.openmarket.items;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class OpenMarketItemsApplication {
    public static void main(String[] args) {
        SpringApplication.run(OpenMarketItemsApplication.class, args);
    }
}
