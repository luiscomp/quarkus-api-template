package com.logicsoftware.v1.address;

import static org.junit.jupiter.api.Assertions.assertEquals;

import javax.inject.Inject;

import org.junit.jupiter.api.Test;

import com.logicsoftware.database.DatabaseLifecycle;
import com.logicsoftware.dtos.address.ViaCepResponseDTO;
import com.logicsoftware.mocks.AddressMocks;
import com.logicsoftware.services.v1.AddressServiceV1;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
@QuarkusTestResource(DatabaseLifecycle.class)
public class AddressUnitTest {

    @Inject
    AddressServiceV1 addressService;

    @Test
    public void getAddressByCep() {
        ViaCepResponseDTO response = addressService.getAddressByCep("64033660");
        assertEquals(AddressMocks.viaCepAddress(), response);
    }
}
