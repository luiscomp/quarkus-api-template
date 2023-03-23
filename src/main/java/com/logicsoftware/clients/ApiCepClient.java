package com.logicsoftware.clients;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import com.logicsoftware.dtos.address.ApiCepResponseDTO;

@Path("/v1")
@RegisterRestClient(baseUri = "https://opencep.com")
public interface ApiCepClient {
    
    @GET
    @Path("/{cep}")
    @Produces(MediaType.APPLICATION_JSON)
    public ApiCepResponseDTO getAddressByCep(@PathParam("cep") String cep);
}
