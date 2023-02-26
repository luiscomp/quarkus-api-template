package com.logicsoftware.keycloak;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RealmRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.RolesRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.keycloak.util.JsonSerialization;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class KeycloakLifecycle implements QuarkusTestResourceLifecycleManager {

    private GenericContainer<?> keycloak = new GenericContainer<>("quay.io/keycloak/keycloak:19.0.1");

    private static String KEYCLOAK_SERVER_URL;
    private static final int KEYCLOAK_PORT_HTTP = 8080;
    private static final String KEYCLOAK_CONTEXT_PATH = "/";
    private static final String KEYCLOAK_REALM = "quarkus";
    private static final String KEYCLOAK_SERVICE_CLIENT = "flashlight-backend";
    private static final String KEYCLOAK_SERVICE_CLIENT_SECRET = "secret";

    @Override
    public Map<String, String> start() {
        keycloak = keycloak
            .withExposedPorts(KEYCLOAK_PORT_HTTP)
            .withEnv("KEYCLOAK_ADMIN", "admin")
            .withEnv("KEYCLOAK_ADMIN_PASSWORD", "admin");

        keycloak = keycloak.withCommand("start-dev");

        keycloak.setWaitStrategy(Wait
            .forHttp(KEYCLOAK_CONTEXT_PATH)
            .forPort(KEYCLOAK_PORT_HTTP)
            .withStartupTimeout(Duration.ofMinutes(2))
        );

        keycloak.start();

        KEYCLOAK_SERVER_URL = getAuthServerUrl();

        RealmRepresentation realm = createRealm(KEYCLOAK_REALM);
        postRealm(realm);
        
        Map<String, String> properties = Map.of(
            "quarkus.oidc.auth-server-url", KEYCLOAK_SERVER_URL + "realms/" + KEYCLOAK_REALM,
            "quarkus.oidc.client-id", KEYCLOAK_SERVICE_CLIENT,
            "quarkus.oidc.credentials.secret", KEYCLOAK_SERVICE_CLIENT_SECRET,
            "quarkus.smallrye-openapi.oauth2-implicit-authorization-url", KEYCLOAK_SERVER_URL + "realms/" + KEYCLOAK_REALM + "/protocol/openid-connect/auth",
            "quarkus.smallrye-openapi.oidc-open-id-connect-url", KEYCLOAK_SERVER_URL + "realms/" + KEYCLOAK_REALM + "/.well-known/openid-configuration",
            "quarkus.swagger-ui.oauth2-redirect-url", "http://localhost:8081/flashlight/swagger-ui/oauth2-redirect.html"
        );

        return properties;
    }

    private String getAuthServerUrl() {
        return String.format("http://%s:%s%s", keycloak.getHost(), keycloak.getMappedPort(KEYCLOAK_PORT_HTTP), KEYCLOAK_CONTEXT_PATH);
    }

    private static void postRealm(RealmRepresentation realm) {
        try {
            createRequestSpec().auth().oauth2(getAdminAccessToken())
                    .contentType(ContentType.JSON)
                    .body(JsonSerialization.writeValueAsBytes(realm))
                    .when()
                    .post(KEYCLOAK_SERVER_URL + "admin/realms").then()
                    .statusCode(201);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String getAdminAccessToken() {
        return createRequestSpec()
                .contentType(ContentType.URLENC)
                .formParams(Map.of(
                    "username", "admin",
                    "password", "admin",
                    "grant_type", "password",
                    "client_id", "admin-cli"
                ))
                .when()
                .post(KEYCLOAK_SERVER_URL + "realms/master/protocol/openid-connect/token")
                .as(AccessTokenResponse.class).getToken();
    }

    public static String getAccessToken(String userName) {
        return createRequestSpec()
                .contentType(ContentType.URLENC)
                .formParams(Map.of(
                    "username", userName,
                    "password", userName,
                    "grant_type", "password",
                    "client_id", KEYCLOAK_SERVICE_CLIENT,
                    "client_secret", KEYCLOAK_SERVICE_CLIENT_SECRET
                ))
                .when()
                .post(KEYCLOAK_SERVER_URL + "realms/" + KEYCLOAK_REALM + "/protocol/openid-connect/token")
                .as(AccessTokenResponse.class).getToken();
    }

    public static String getRefreshToken(String userName) {
        return createRequestSpec()
                .contentType(ContentType.URLENC)
                .formParams(Map.of(
                    "username", userName,
                    "password", userName,
                    "grant_type", "password",
                    "client_id", KEYCLOAK_SERVICE_CLIENT,
                    "client_secret", KEYCLOAK_SERVICE_CLIENT_SECRET
                ))
                .when()
                .post(KEYCLOAK_SERVER_URL + "realms/" + KEYCLOAK_REALM + "/protocol/openid-connect/token")
                .as(AccessTokenResponse.class).getRefreshToken();
    }

    private static RealmRepresentation createRealm(String name) {
        RealmRepresentation realm = new RealmRepresentation();

        realm.setRealm(name);
        realm.setEnabled(true);
        realm.setUsers(new ArrayList<>());
        realm.setClients(new ArrayList<>());

        RolesRepresentation roles = new RolesRepresentation();
        List<RoleRepresentation> realmRoles = new ArrayList<>();

        roles.setRealm(realmRoles);
        realm.setRoles(roles);

        realm.getRoles().getRealm().add(new RoleRepresentation("user", null, false));
        realm.getRoles().getRealm().add(new RoleRepresentation("admin", null, false));
        realm.getRoles().getRealm().add(new RoleRepresentation("confidential", null, false));

        realm.getClients().add(createServiceClient(KEYCLOAK_SERVICE_CLIENT, KEYCLOAK_SERVICE_CLIENT_SECRET));

        realm.getUsers().add(createUser("alice", List.of("user", "default-roles-quarkus ")));
        realm.getUsers().add(createUser("admin", List.of("user", "admin", "default-roles-quarkus ")));
        realm.getUsers().add(createUser("jdoe", List.of("user", "confidential", "default-roles-quarkus ")));

        return realm;
    }

    private static ClientRepresentation createServiceClient(String clientId, String clientSecret) {
        ClientRepresentation client = new ClientRepresentation();

        client.setClientId(clientId);
        client.setSecret(clientSecret);
        client.setPublicClient(false);
        client.setDirectAccessGrantsEnabled(true);
        client.setStandardFlowEnabled(true);
        client.setServiceAccountsEnabled(true);
        client.setRedirectUris(Arrays.asList("*"));
        client.setWebOrigins(Arrays.asList("*"));
        client.setAuthorizationServicesEnabled(true);
        client.setEnabled(true);

        return client;
    }

    private static UserRepresentation createUser(String username, List<String> realmRoles) {
        UserRepresentation user = new UserRepresentation();

        user.setUsername(username);
        user.setEnabled(true);
        user.setCredentials(new ArrayList<>());
        user.setRealmRoles(realmRoles);
        user.setEmail(username + "@gmail.com");
        user.setEmailVerified(true);

        CredentialRepresentation credential = new CredentialRepresentation();

        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(username);
        credential.setTemporary(false);

        user.getCredentials().add(credential);

        return user;
    }

    @Override
    public void stop() {
        createRequestSpec().auth().oauth2(getAdminAccessToken())
                .when()
                .delete(KEYCLOAK_SERVER_URL + "admin/realms/" + KEYCLOAK_REALM).then().statusCode(204);

        keycloak.stop();
    }

    private static RequestSpecification createRequestSpec() {
        return RestAssured.given();
    }
}
