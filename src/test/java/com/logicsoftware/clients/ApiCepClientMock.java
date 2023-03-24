package com.logicsoftware.clients;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import com.logicsoftware.dtos.address.ApiCepResponseDTO;
import com.logicsoftware.v2.mocks.AddressResourceMocksV2;

import io.quarkus.test.Mock;


@Mock
@RestClient
public class ApiCepClientMock implements ApiCepClient {

    @Override
    public ApiCepResponseDTO getAddressByCep(String cep) {
        return AddressResourceMocksV2.apiCepAddress();
    }
}
