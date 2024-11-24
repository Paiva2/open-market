package org.com.openmarket.customuserstorage.providers;

import lombok.ToString;
import org.com.openmarket.customuserstorage.entity.UserEntityImpl;
import org.keycloak.component.ComponentModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.storage.StorageId;
import org.keycloak.storage.adapter.AbstractUserAdapterFederatedStorage;

@ToString
public class UserAdapter extends AbstractUserAdapterFederatedStorage {
    private final UserEntityImpl userEntityImpl;
    private final String id;

    public UserAdapter(KeycloakSession session, RealmModel realm, ComponentModel storageProviderModel, UserEntityImpl userEntityImpl) {
        super(session, realm, storageProviderModel);
        this.userEntityImpl = userEntityImpl;
        this.id = StorageId.keycloakId(storageProviderModel, String.valueOf(userEntityImpl.getId()));
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getUsername() {
        return userEntityImpl.getUserName();
    }

    @Override
    public void setUsername(String username) {
        userEntityImpl.setUserName(username);
    }

    @Override
    public String getEmail() {
        return userEntityImpl.getEmail();
    }

    @Override
    public void setEmail(String email) {
        userEntityImpl.setEmail(email);
    }

    public String getPassword() {
        return userEntityImpl.getPassword();
    }

    public void setPassword(String password) {
        userEntityImpl.setPassword(password);
    }
}
