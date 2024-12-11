package org.com.openmarket.gateway.application.config.mongo;

import com.github.mongobee.Mongobee;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MongoConfig {
    @Value("${mongodb.uri}")
    private String DB_URI;

    @Value("${mongodb.database}")
    private String DB_NAME;

    @Value("${mongodb.logsscan-packages}")
    private String PACKAGES_PATH;

    @Bean
    public Mongobee mongobee(){
        Mongobee runner = new Mongobee(DB_URI);
        runner.setDbName(DB_NAME);
        runner.setChangeLogsScanPackage(PACKAGES_PATH);

        return runner;
    }
}
