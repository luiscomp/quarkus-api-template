package com.logicsoftware.keycloak;

import java.time.Duration;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

import org.apache.commons.io.FilenameUtils;
import org.keycloak.representations.AccessTokenResponse;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.MountableFile;

import com.github.dockerjava.api.command.CreateContainerCmd;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class KeycloakLifecycle implements QuarkusTestResourceLifecycleManager {

    private GenericContainer<?> keycloak = new GenericContainer<>("quay.io/keycloak/keycloak:19.0.1");

    private static String KEYCLOAK_SERVER_URL;
    private static final int KEYCLOAK_EXPOSED_PORT_HTTP = 8080;
    private static final int KEYCLOAK_BINDING_PORT_HTTP = 3200;
    private static final String KEYCLOAK_CONTEXT_PATH = "/";
    private static final String KEYCLOAK_REALM = "quarkus";
    private static final String KEYCLOAK_SERVICE_CLIENT = "flashlight-backend";
    private static final String KEYCLOAK_SERVICE_CLIENT_SECRET = UUID.randomUUID().toString();
    private static final String DEFAULT_REALM_IMPORT_FILES_LOCATION = "/opt/keycloak/data/import/";
    private static final String REALM_FILE = "/keycloak/quarkus-realm.json";

    @Override
    public Map<String, String> start() {
        Consumer<CreateContainerCmd> cmd = e -> 
            e.getHostConfig().withPortBindings(new PortBinding(Ports.Binding.bindPort(KEYCLOAK_BINDING_PORT_HTTP), new ExposedPort(KEYCLOAK_EXPOSED_PORT_HTTP)));

        keycloak = keycloak
            .withExposedPorts(KEYCLOAK_EXPOSED_PORT_HTTP)
            .withCreateContainerCmdModifier(cmd)
            .withEnv("KEYCLOAK_ADMIN", "admin")
            .withEnv("KEYCLOAK_ADMIN_PASSWORD", "admin");


        String importFileInContainer = DEFAULT_REALM_IMPORT_FILES_LOCATION + FilenameUtils.getName(REALM_FILE);
        keycloak.withCopyFileToContainer(MountableFile.forClasspathResource(REALM_FILE), importFileInContainer);
        // keycloak.setCommandParts(new String[]{ "start-dev", "--import-realm" });
        keycloak = keycloak.withCommand("start-dev --import-realm");

        keycloak.setWaitStrategy(Wait
            .forHttp(KEYCLOAK_CONTEXT_PATH)
            .forPort(KEYCLOAK_EXPOSED_PORT_HTTP)
            .withStartupTimeout(Duration.ofMinutes(2))
        );

        keycloak.start();

        KEYCLOAK_SERVER_URL = getAuthServerUrl();
        
        Map<String, String> properties = Map.of(
            "quarkus.oidc.auth-server-url", KEYCLOAK_SERVER_URL + "realms/" + KEYCLOAK_REALM,
            "quarkus.oidc.client-id", KEYCLOAK_SERVICE_CLIENT,
            "quarkus.oidc.credentials.secret", KEYCLOAK_SERVICE_CLIENT_SECRET
        );

        return properties;
    }

    private String getAuthServerUrl() {
        return String.format("http://%s:%s%s", keycloak.getHost(), keycloak.getMappedPort(KEYCLOAK_EXPOSED_PORT_HTTP), KEYCLOAK_CONTEXT_PATH);
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
