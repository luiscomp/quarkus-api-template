package com.logicsoftware.exceptions.handlers;

import com.logicsoftware.utils.request.DataResponse;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class NotFoundHandler implements ExceptionMapper<NotFoundException> {

    @Override
    public Response toResponse(NotFoundException exception) {
        DataResponse.DataResponseBuilder<?> response = DataResponse.builder();
        response.message(exception.getMessage());
        return Response.status(Response.Status.NOT_FOUND).entity(response.build()).build();
    }
}
