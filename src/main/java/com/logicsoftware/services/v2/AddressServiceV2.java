package com.logicsoftware.services.v2;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;

import com.logicsoftware.clients.ApiCepClient;
import com.logicsoftware.dtos.address.ApiCepResponseDTO;
import com.logicsoftware.services.AddressService;

import io.quarkus.arc.Unremovable;
import io.quarkus.arc.log.LoggerName;

@Unremovable
@ApplicationScoped
public class AddressServiceV2 implements AddressService {
    
    @LoggerName("address-service-v2")
    Logger logger;

    @Inject
    @RestClient
    ApiCepClient apiCepClient;

    @Override
    public ApiCepResponseDTO getAddressByCep(String cep) {
        return apiCepClient.getAddressByCep(cep);
    }
}
