package com.logicsoftware.exceptions.handlers;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Map;
import java.util.Objects;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.jboss.resteasy.spi.ApplicationException;

import com.logicsoftware.utils.request.DataResponse;

@Provider
public class ApplicationHandler implements ExceptionMapper<ApplicationException> {
    @Override
    public Response toResponse(ApplicationException exception) {
        DataResponse.DataResponseBuilder<?> response = DataResponse.builder();

        SQLIntegrityConstraintViolationException sqlIntegrityException = findConstraintViolationException(exception);
        if(Objects.nonNull(sqlIntegrityException)) {
            response.message("SQL Integrity Constraint Violation");
            response.errors(Map.of("integrity", sqlIntegrityException.getMessage()));
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(response.build()).build();
        }

        response.message("Error");
        response.errors(Map.of("Exception", exception.getMessage()));
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(response.build()).build();
    }

    private SQLIntegrityConstraintViolationException findConstraintViolationException(Throwable exception) {
        while (exception.getCause() != null && exception.getCause() != exception) {
            exception = exception.getCause();
            if (exception.getClass().equals(SQLIntegrityConstraintViolationException.class)) {
                return (SQLIntegrityConstraintViolationException) exception;
            }
        }
        return null;
    }
}


