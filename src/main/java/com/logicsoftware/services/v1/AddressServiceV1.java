package com.logicsoftware.services.v1;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;

import com.logicsoftware.clients.ViaCepClient;
import com.logicsoftware.dtos.address.ViaCepResponseDTO;
import com.logicsoftware.services.AddressService;

import io.quarkus.arc.Unremovable;
import io.quarkus.arc.log.LoggerName;

@Unremovable
@ApplicationScoped
public class AddressServiceV1 implements AddressService {
    
    @LoggerName("address-service-v1")
    Logger logger;

    @Inject
    @RestClient
    ViaCepClient viaCepClient;

    @Override
    public ViaCepResponseDTO getAddressByCep(String cep) {
        return viaCepClient.getAddressByCep(cep);
    }
}
