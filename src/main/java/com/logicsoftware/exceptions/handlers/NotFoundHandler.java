package com.logicsoftware.exceptions.handlers;

import java.util.Map;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.logicsoftware.utils.enums.ResponseStatus;
import com.logicsoftware.utils.request.ErrorResponse;

@Provider
public class NotFoundHandler implements ExceptionMapper<NotFoundException> {

    @Override
    public Response toResponse(NotFoundException exception) {
        ErrorResponse.ErrorResponseBuilder response = ErrorResponse.builder();
        response.status(ResponseStatus.ERROR);
        response.exception(exception.getClass());
        response.errors(Map.of("message", exception.getMessage()));
        return Response.status(Response.Status.NOT_FOUND).entity(response.build()).build();
    }
}
