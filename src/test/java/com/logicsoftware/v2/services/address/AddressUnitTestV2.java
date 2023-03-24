package com.logicsoftware.v2.services.address;

import static org.junit.jupiter.api.Assertions.assertEquals;

import javax.inject.Inject;

import org.junit.jupiter.api.Test;

import com.logicsoftware.database.DatabaseLifecycle;
import com.logicsoftware.dtos.address.ApiCepResponseDTO;
import com.logicsoftware.services.v2.AddressServiceV2;
import com.logicsoftware.v2.mocks.AddressResourceMocksV2;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
@QuarkusTestResource(value = DatabaseLifecycle.class, parallel = true)
public class AddressUnitTestV2 {

    @Inject
    AddressServiceV2 addressService;

    @Test
    public void getAddressByCep() {
        ApiCepResponseDTO response = addressService.getAddressByCep("64033660");
        assertEquals(AddressResourceMocksV2.apiCepAddress(), response);
    }
}
