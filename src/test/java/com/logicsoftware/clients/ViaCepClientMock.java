package com.logicsoftware.clients;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import com.logicsoftware.address.AddressMocks;
import com.logicsoftware.dtos.address.ViaCepResponseDTO;

import io.quarkus.test.Mock;


@Mock
@RestClient
public class ViaCepClientMock extends AddressMocks implements ViaCepClient {

    @Override
    public ViaCepResponseDTO getAddressByCep(String cep) {
        return addressByCepMock();
    }
    
}
