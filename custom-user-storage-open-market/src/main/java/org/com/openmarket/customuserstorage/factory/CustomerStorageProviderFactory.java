package org.com.openmarket.customuserstorage.factory;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import lombok.extern.slf4j.Slf4j;
import org.com.openmarket.customuserstorage.providers.CustomerStorageProvider;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.keycloak.component.ComponentModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.storage.UserStorageProviderFactory;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class CustomerStorageProviderFactory implements UserStorageProviderFactory<CustomerStorageProvider> {
    private final static String PROVIDER_ID = "custom-user-storage";

    private EntityManagerFactory entityManagerFactory;

    @Override
    public CustomerStorageProvider create(KeycloakSession keycloakSession, ComponentModel componentModel) {
        return new CustomerStorageProvider(keycloakSession, componentModel, createEntityManager());
    }

    private EntityManager createEntityManager() {
        if (entityManagerFactory == null) {
            createEntityManagerFactory();
        }

        return entityManagerFactory.createEntityManager();
    }

    synchronized private void createEntityManagerFactory() {
        HibernatePersistenceProvider persistenceProvider = new HibernatePersistenceProvider();
        Map<String, Object> props = new HashMap<>() {{
            put("hibernate.connection.url", "jdbc:postgresql://open-market-users-db:5432/open-market-users-db");
            put("hibernate.connection.username", "development");
            put("hibernate.connection.password", "development");
            put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        }};

        entityManagerFactory = persistenceProvider.createEntityManagerFactory("user-store", props);
    }

    @Override
    public String getId() {
        return PROVIDER_ID;
    }

    @Override
    public void close() {
        if (entityManagerFactory != null && entityManagerFactory.isOpen()) {
            log.warn("Closing Database connection!");
            entityManagerFactory.close();
            entityManagerFactory = null;
            log.warn("Database connection closed!");
        }
    }
}
