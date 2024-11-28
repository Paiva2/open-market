package org.com.openmarket.customuserstorage;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableRabbit
public class CustomUserStorageOpenMarketApplication {

    public static void main(String[] args) {
        SpringApplication.run(CustomUserStorageOpenMarketApplication.class, args);
    }

}
