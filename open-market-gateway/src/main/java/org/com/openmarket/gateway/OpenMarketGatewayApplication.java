package org.com.openmarket.gateway;

import io.mongock.runner.springboot.EnableMongock;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
@EnableMongock
public class OpenMarketGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(OpenMarketGatewayApplication.class, args);
    }
}
