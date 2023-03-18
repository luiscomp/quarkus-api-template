package com.logicsoftware.database;

import java.util.Map;
import java.util.Optional;
// import java.util.function.Consumer;

import org.testcontainers.containers.MySQLContainer;

// import com.github.dockerjava.api.command.CreateContainerCmd;
// import com.github.dockerjava.api.model.ExposedPort;
// import com.github.dockerjava.api.model.PortBinding;
// import com.github.dockerjava.api.model.Ports;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;

public class DatabaseLifecycle implements QuarkusTestResourceLifecycleManager {

    // private static final int MYSQL_EXPOSED_PORT = 3306;
    // private static final int MYSQL_BINDING_PORT = 3506;
    private static MySQLContainer<?> MYSQL = new MySQLContainer<>("mysql:8.0.30");

    @Override
    public Map<String, String> start() {
        // Consumer<CreateContainerCmd> cmd = e -> e.getHostConfig().withPortBindings(new PortBinding(Ports.Binding.bindPort(MYSQL_BINDING_PORT), new ExposedPort(MYSQL_EXPOSED_PORT)));

        // MYSQL
        // .withExposedPorts(MYSQL_EXPOSED_PORT)
        // .withCreateContainerCmdModifier(cmd);

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
