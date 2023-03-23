package com.logicsoftware.services.v2;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;

import com.logicsoftware.clients.ViaCepClient;
import com.logicsoftware.dtos.address.ViaCepResponseDTO;
import com.logicsoftware.services.AddressService;
import com.logicsoftware.utils.i18n.Messages;

import io.quarkus.arc.Unremovable;
import io.quarkus.arc.log.LoggerName;

@Unremovable
@ApplicationScoped
public class AddressServiceImpl implements AddressService {
    
    @LoggerName("address-service")
    Logger logger;

    @Inject
    Messages message;

    @Inject
    @RestClient
    ViaCepClient viaCepClient;

    @Override
    public ViaCepResponseDTO getAddressByCep(String cep) {
        return viaCepClient.getAddressByCep("64000100");
    }
}