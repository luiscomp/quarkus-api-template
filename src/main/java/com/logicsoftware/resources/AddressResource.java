package com.logicsoftware.resources;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameters;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import com.logicsoftware.dtos.address.ViaCepResponseDTO;
import com.logicsoftware.services.AddressService;
import com.logicsoftware.utils.enums.ResponseStatus;
import com.logicsoftware.utils.request.DataResponse;

@Tag(name = "Address")
@Path("/address")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AddressResource {

    @Inject
    AddressService addressService;


    @GET
    @Path("/cep/{cep}")
    @Operation(
        summary = "Get a Address by CEP",
        description = "Pass a CEP to get a Address"
    )

    @Parameters({
        @Parameter(name = "cep", description = "Address CEP", required = true, example = "64033660")
    })
    public DataResponse<ViaCepResponseDTO> getAddressByCep(@PathParam("cep") String cep) {
        DataResponse.DataResponseBuilder<ViaCepResponseDTO> response = DataResponse.builder();
        response.data(addressService.getAddressByCep(cep));
        response.status(ResponseStatus.SUCCESS);
        return response.build();
    }
    
}
