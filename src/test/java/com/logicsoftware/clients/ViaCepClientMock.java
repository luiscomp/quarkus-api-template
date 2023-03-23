package com.logicsoftware.clients;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import com.logicsoftware.dtos.address.ViaCepResponseDTO;
import com.logicsoftware.mocks.AddressMocks;

import io.quarkus.test.Mock;


@Mock
@RestClient
public class ViaCepClientMock implements ViaCepClient {

    @Override
    public ViaCepResponseDTO getAddressByCep(String cep) {
        return AddressMocks.viaCepAddress();
    }
}
