package org.com.openmarket.users;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class OpenMarketUsersApplication {
	public static void main(String[] args) {
		SpringApplication.run(OpenMarketUsersApplication.class, args);
	}
}
