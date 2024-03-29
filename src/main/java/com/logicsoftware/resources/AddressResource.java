package com.logicsoftware.resources;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameters;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import com.logicsoftware.dtos.address.ApiCepResponseDTO;
import com.logicsoftware.dtos.address.ViaCepResponseDTO;
import com.logicsoftware.exceptions.ResourceNotFoundException;
import com.logicsoftware.services.AddressService;
import com.logicsoftware.utils.BeanGenerator;
import com.logicsoftware.utils.enums.AppStatus;
import com.logicsoftware.utils.i18n.Messages;
import com.logicsoftware.utils.request.DataResponse;

@Tag(name = "Address")
@Path("/address/v{version}")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AddressResource {

    @Inject
    BeanGenerator beanGenerator;

    @Inject
    Messages message;

    @GET
    @Path("/cep/{cep}")
    @Operation(summary = "Get a Address by CEP", description = "Pass a CEP to get a Address")
    @APIResponses({
            @APIResponse(name = "ViaCep", responseCode = "200.v1", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ViaCepResponseDTO.class))),
            @APIResponse(name = "ApiCep", responseCode = "200.v2", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiCepResponseDTO.class)))
    })
    @Parameters({
            @Parameter(name = "cep", description = "Address CEP", required = true, example = "64033660"),
            @Parameter(name = "version", description = "Version of the API", required = true, example = "1")
    })
    public DataResponse<Object> getAddressByCep(@PathParam("cep") String cep, @PathParam("version") String version) {
        AddressService addressService = beanGenerator.getInstance(AddressService.class, version)
                .orElseThrow(
                        () -> new ResourceNotFoundException(message.getMessage("api.resource.not.found", version)));

        DataResponse.DataResponseBuilder<Object> response = DataResponse.builder();
        response.data(addressService.getAddressByCep(cep));
        response.status(AppStatus.SUCCESS);
        return response.build();
    }

}
