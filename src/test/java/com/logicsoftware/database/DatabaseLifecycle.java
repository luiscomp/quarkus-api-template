package com.logicsoftware.database;

import java.util.Map;
import java.util.Optional;

import org.testcontainers.containers.MySQLContainer;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;

public class DatabaseLifecycle implements QuarkusTestResourceLifecycleManager {

    private static MySQLContainer<?> MYSQL = new MySQLContainer<>("mysql:8.0.30");

    @Override
    public Map<String, String> start() {
        MYSQL.start();
        Map<String, String> properties = Map.of(
            "quarkus.datasource.jdbc.url", MYSQL.getJdbcUrl(),
            "quarkus.datasource.username", MYSQL.getUsername(),
            "quarkus.datasource.password", MYSQL.getPassword()
        );
        return properties;
    }

    @Override
    public void stop() {
        Optional.ofNullable(MYSQL).ifPresent(MySQLContainer::stop);
    }
}
