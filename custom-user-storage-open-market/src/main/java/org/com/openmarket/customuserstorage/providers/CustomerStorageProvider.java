package org.com.openmarket.customuserstorage.providers;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.ws.rs.core.MultivaluedMap;
import lombok.extern.slf4j.Slf4j;
import org.com.openmarket.customuserstorage.entity.UserEntityImpl;
import org.com.openmarket.customuserstorage.providers.dto.UserCreatedMessageDTO;
import org.com.openmarket.customuserstorage.providers.enumeration.EnumUserEvents;
import org.keycloak.component.ComponentModel;
import org.keycloak.credential.CredentialInput;
import org.keycloak.credential.CredentialInputUpdater;
import org.keycloak.credential.CredentialInputValidator;
import org.keycloak.models.*;
import org.keycloak.models.credential.PasswordCredentialModel;
import org.keycloak.storage.StorageId;
import org.keycloak.storage.UserStorageProvider;
import org.keycloak.storage.user.UserLookupProvider;
import org.keycloak.storage.user.UserQueryProvider;
import org.keycloak.storage.user.UserRegistrationProvider;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.com.openmarket.customuserstorage.constants.QueueConstants.USER_DATA_ROUTING_KEY;
import static org.com.openmarket.customuserstorage.constants.QueueConstants.USER_DATA_TOPIC_EXCHANGE;

@Slf4j
public class CustomerStorageProvider implements UserStorageProvider, UserLookupProvider, UserQueryProvider, UserRegistrationProvider, CredentialInputValidator, CredentialInputUpdater {
    private final KeycloakSession session;
    private final ComponentModel model;
    private final EntityManager entityManager;
    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper mapper = new ObjectMapper();

    private final Queue userDataQueue;

    public CustomerStorageProvider(KeycloakSession keycloakSession, ComponentModel componentModel, EntityManager entityManager, RabbitTemplate rabbitTemplate, Queue userDataQueue) {
        this.session = keycloakSession;
        this.model = componentModel;
        this.entityManager = entityManager;
        this.rabbitTemplate = rabbitTemplate;
        this.userDataQueue = userDataQueue;
    }

    @Override
    public void close() {

    }

    @Override
    public UserModel getUserById(RealmModel realmModel, String id) {
        Long persistenceId = Long.parseLong(StorageId.externalId(id));

        TypedQuery<UserEntityImpl> query = entityManager.createQuery(
            "select u from UserEntityImpl u where u.id = :userId",
            UserEntityImpl.class
        );
        query.setParameter("userId", persistenceId);

        List<UserEntityImpl> user = query.getResultList();

        if (user.isEmpty()) return null;

        return new UserAdapter(session, realmModel, model, user.get(0));
    }

    @Override
    public UserModel getUserByUsername(RealmModel realmModel, String username) {
        TypedQuery<UserEntityImpl> query = entityManager.createQuery(
            "select u from UserEntityImpl u where u.userName = :username",
            UserEntityImpl.class
        );
        query.setParameter("username", username);

        List<UserEntityImpl> user = query.getResultList();

        if (user.isEmpty()) return null;

        return new UserAdapter(session, realmModel, model, user.get(0));
    }

    @Override
    public UserModel getUserByEmail(RealmModel realmModel, String email) {
        TypedQuery<UserEntityImpl> query = entityManager.createQuery(
            "select u from UserEntityImpl u where u.email = :email",
            UserEntityImpl.class
        );
        query.setParameter("email", email);

        List<UserEntityImpl> user = query.getResultList();

        if (user.isEmpty()) return null;

        return new UserAdapter(session, realmModel, model, user.get(0));
    }

    @Override
    public Stream<UserModel> searchForUserStream(RealmModel realmModel, Map<String, String> map, Integer firstResult, Integer maxResults) {
        String searchParam = map.get("keycloak.session.realm.users.query.search");

        TypedQuery<UserEntityImpl> query = entityManager.createQuery(
            "select u from UserEntityImpl u " +
                "where lower(u.userName) " +
                "like concat('%', lower(:search), '%') " +
                "or lower(u.email) = lower(:search) " +
                "order by u.userName limit :maxResults",
            UserEntityImpl.class
        );
        query.setParameter("search", searchParam);
        query.setParameter("maxResults", maxResults);
        Stream<UserEntityImpl> usersList = query.getResultStream();

        return usersList.map(userFound -> new UserAdapter(session, realmModel, model, userFound));
    }

    @Override
    public Stream<UserModel> getGroupMembersStream(RealmModel realmModel, GroupModel groupModel, Integer integer, Integer integer1) {
        return Stream.empty();
    }

    @Override
    public Stream<UserModel> searchForUserByUserAttributeStream(RealmModel realmModel, String s, String s1) {
        return Stream.empty();
    }

    @Override
    public UserModel addUser(RealmModel realmModel, String username) {
        KeycloakContext context = session.getContext();
        MultivaluedMap<String, String> formParams = context.getHttpRequest().getDecodedFormParameters();

        String email = formParams.getFirst("email");
        String password = formParams.getFirst("password");
        String formUsername = formParams.getFirst("username");
        String firstName = formParams.getFirst("firstName");
        String lastName = formParams.getFirst("lastName");

        String hashedNewPassword = BCrypt.hashpw(password, BCrypt.gensalt(6));

        try {
            entityManager.getTransaction().begin();
            Query query = entityManager.createQuery(
                "insert into UserEntityImpl (email, password, userName) values (:email, :password, :username)"
            );
            query.setParameter("email", email);
            query.setParameter("password", hashedNewPassword);
            query.setParameter("username", formUsername);
            query.executeUpdate();
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            log.error("Error while creating new user on database {}", e.getMessage());
            throw new RuntimeException("Error while creating new user on database {}", e);
        }

        UserModel userModel = this.getUserByEmail(realmModel, email);

        try {
            rabbitTemplate.convertAndSend(
                USER_DATA_TOPIC_EXCHANGE,
                USER_DATA_ROUTING_KEY,
                mapper.writeValueAsString(new UserCreatedMessageDTO(EnumUserEvents.CREATED, StorageId.externalId(userModel.getId()), userModel.getUsername(), userModel.getEmail()))
            );
        } catch (Exception e) {
            log.error("Error while sending new user to broker {}", e.getMessage());
            entityManager.getTransaction().begin();
            Query query = entityManager.createQuery("delete from UserEntityImpl u where u.id = :userId");
            query.setParameter("userId", StorageId.externalId(userModel.getId()));
            query.executeUpdate();
            entityManager.getTransaction().commit();
            throw new RuntimeException("Error while sending new user to broker! User creation failed.");
        }

        userModel.setEnabled(true);
        userModel.setFirstName(firstName);
        userModel.setLastName(lastName);
        userModel.setEmail(email);
        userModel.setUsername(formUsername);
        userModel.credentialManager().updateCredential(UserCredentialModel.password(hashedNewPassword));

        return userModel;
    }

    @Override
    public boolean removeUser(RealmModel realmModel, UserModel userModel) {
        StorageId sid = new StorageId(userModel.getId());
        String persistenceId = sid.getExternalId();
        if (persistenceId == null) return false;

        try {
            entityManager.getTransaction().begin();
            Query query = entityManager.createQuery("delete from UserEntityImpl u where u.id = :userId");
            query.setParameter("userId", persistenceId);
            query.executeUpdate();
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            log.error("Error while removing user: {}", e.getMessage(), e);
            return false;
        }

        return true;
    }

    @Override
    public boolean supportsCredentialType(String type) {
        return PasswordCredentialModel.TYPE.endsWith(type);
    }

    @Override
    public boolean updateCredential(RealmModel realmModel, UserModel userModel, CredentialInput credentialInput) {
        if (!supportsCredentialType(credentialInput.getType())) return false;

        StorageId sid = new StorageId(userModel.getId());
        String userExternalId = sid.getExternalId();

        String passwordInput = credentialInput.getChallengeResponse();

        if (userExternalId == null || passwordInput == null) return false;

        TypedQuery<UserEntityImpl> query = entityManager.createQuery(
            "select u from UserEntityImpl u where u.id = :externalId ",
            UserEntityImpl.class
        );
        query.setParameter("externalId", userExternalId);

        List<UserEntityImpl> user = query.getResultList();

        if (user.isEmpty()) return false;

        UserEntityImpl userFound = user.get(0);

        String hashedNewPassword = BCrypt.hashpw(passwordInput, BCrypt.gensalt(6));
        userFound.setPassword(hashedNewPassword);
        userFound.setEmail(userModel.getEmail());
        userFound.setUserName(userModel.getUsername());

        entityManager.getTransaction().begin();
        Query queryUpdate = entityManager.createQuery(
            "update UserEntityImpl " +
                "set password = :password, " +
                "userName = :userName, " +
                "email = :email " +
                "where id = :externalId"
        );
        queryUpdate.setParameter("externalId", userExternalId);
        queryUpdate.setParameter("password", hashedNewPassword);
        queryUpdate.setParameter("userName", userFound.getUserName());
        queryUpdate.setParameter("email", userFound.getEmail());
        queryUpdate.executeUpdate();
        entityManager.getTransaction().commit();

        return true;
    }

    @Override
    public void disableCredentialType(RealmModel realmModel, UserModel userModel, String s) {

    }

    @Override
    public Stream<String> getDisableableCredentialTypesStream(RealmModel realmModel, UserModel userModel) {
        return Stream.empty();
    }

    @Override
    public boolean isConfiguredFor(RealmModel realmModel, UserModel userModel, String type) {
        return type != null && supportsCredentialType(type);
    }

    @Override
    public boolean isValid(RealmModel realmModel, UserModel userModel, CredentialInput credentialInput) {
        if (!supportsCredentialType(credentialInput.getType())) return false;

        StorageId sid = new StorageId(userModel.getId());
        String userExternalId = sid.getExternalId();
        userModel.setSingleAttribute("externalId", userExternalId);

        String passwordInput = credentialInput.getChallengeResponse();

        if (userExternalId == null || passwordInput == null) return false;

        TypedQuery<UserEntityImpl> query = entityManager.createQuery(
            "select u from UserEntityImpl u " +
                "where u.id = :externalId ",
            UserEntityImpl.class
        );
        query.setParameter("externalId", userExternalId);

        List<UserEntityImpl> user = query.getResultList();

        if (user.isEmpty()) return false;

        String userHashPassword = user.get(0).getPassword();

        return BCrypt.checkpw(passwordInput, userHashPassword);
    }

    @Override
    public int getUsersCount(RealmModel realm) {
        TypedQuery<Long> query = entityManager.createQuery("select count(u) from UserEntityImpl u", Long.class);
        return query.getSingleResult().intValue();
    }
}
