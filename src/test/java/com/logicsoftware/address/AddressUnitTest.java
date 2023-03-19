package com.logicsoftware.address;

import static org.junit.jupiter.api.Assertions.assertEquals;

import javax.inject.Inject;

import org.junit.jupiter.api.Test;

import com.logicsoftware.dtos.address.ViaCepResponseDTO;
import com.logicsoftware.services.AddressService;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class AddressUnitTest extends AddressMocks {
    

    @Inject
    AddressService addressService;

    @Test
    public void getAddressByCep() {
        ViaCepResponseDTO response = addressService.getAddressByCep("64033660");
        assertEquals(addressByCepMock(), response);
    }
}
