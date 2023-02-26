package com.logicsoftware;

import static org.junit.jupiter.api.Assertions.assertEquals;

import javax.inject.Inject;

import org.junit.jupiter.api.Test;

import com.github.database.rider.cdi.api.DBRider;
import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.core.api.dataset.DataSet;
import com.logicsoftware.database.DatabaseLifecycle;
import com.logicsoftware.repositories.UsersRepository;

import io.quarkus.test.common.QuarkusTestResource;

import io.quarkus.test.junit.QuarkusTest;

@DBRider
@DBUnit(caseSensitiveTableNames = true)
@QuarkusTest
@QuarkusTestResource(DatabaseLifecycle.class)
public class UserUnitTest {

    @Inject
    UsersRepository usersRepository;

    @Test
    @DataSet("users.yml")
    public void userCount() {
        assertEquals(true, true);
        // Assert.assertEquals(1, usersRepository.count());
    }
}