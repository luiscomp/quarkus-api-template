package com.logicsoftware.clients;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import com.logicsoftware.dtos.address.ViaCepResponseDTO;

@Path("/ws")
@RegisterRestClient(baseUri = "https://viacep.com.br")
public interface ViaCepClient {
    
    @GET
    @Path("/{cep}/json")
    @Produces(MediaType.APPLICATION_JSON)
    public ViaCepResponseDTO getAddressByCep(@PathParam("cep") String cep);
}
