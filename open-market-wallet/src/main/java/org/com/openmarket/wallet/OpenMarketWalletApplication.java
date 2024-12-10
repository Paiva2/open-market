package org.com.openmarket.wallet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class OpenMarketWalletApplication {
	public static void main(String[] args) {
		SpringApplication.run(OpenMarketWalletApplication.class, args);
	}
}
