package com.logicsoftware.user;

import static org.junit.jupiter.api.Assertions.assertEquals;

import javax.inject.Inject;

import org.junit.jupiter.api.Test;

import com.github.database.rider.cdi.api.DBRider;
import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.core.api.dataset.DataSet;
import com.logicsoftware.database.DatabaseLifecycle;
import com.logicsoftware.services.UsersService;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;

@DBRider
@DBUnit(caseSensitiveTableNames = true)
@QuarkusTest
@QuarkusTestResource(DatabaseLifecycle.class)
public class UserUnitTest {

    @Inject
    UsersService usersService;

    @Test
    @DataSet("users.yml")
    public void userCount() {
        assertEquals(1, usersService.findAll(null, 1, 10).size());
    }
}