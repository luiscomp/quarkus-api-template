package com.logicsoftware.v1.services.address;

import static org.junit.jupiter.api.Assertions.assertEquals;

import javax.inject.Inject;

import org.junit.jupiter.api.Test;

import com.logicsoftware.database.DatabaseLifecycle;
import com.logicsoftware.dtos.address.ViaCepResponseDTO;
import com.logicsoftware.services.v1.AddressServiceV1;
import com.logicsoftware.v1.mocks.AddressResourceMocksV1;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
@QuarkusTestResource(value = DatabaseLifecycle.class, parallel = true)
public class AddressUnitTestV1 {

    @Inject
    AddressServiceV1 addressService;

    @Test
    public void getAddressByCep() {
        ViaCepResponseDTO response = addressService.getAddressByCep("64033660");
        assertEquals(AddressResourceMocksV1.viaCepAddress(), response);
    }
}
