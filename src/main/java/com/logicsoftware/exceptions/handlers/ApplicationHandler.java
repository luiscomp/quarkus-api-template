package com.logicsoftware.exceptions.handlers;

import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.Path.Node;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.jboss.logging.Logger;
import org.jboss.resteasy.spi.ApplicationException;

import com.logicsoftware.utils.enums.AppStatus;
import com.logicsoftware.utils.request.ErrorResponse;

import io.quarkus.arc.log.LoggerName;
import io.quarkus.hibernate.validator.runtime.jaxrs.ResteasyViolationExceptionImpl;

@Provider
public class ApplicationHandler implements ExceptionMapper<ApplicationException> {

    @LoggerName("application-handler")
    Logger logger;

    @Override
    public Response toResponse(ApplicationException exception) {
        ErrorResponse.ErrorResponseBuilder response = ErrorResponse.builder();

        Throwable rootCause = getRootException(exception);
        response.status(AppStatus.ERROR);
        response.exception(rootCause.getClass());
        response.errors(Map.of("message", rootCause.getMessage()));

        if(rootCause.getClass().equals(ResteasyViolationExceptionImpl.class)) {
            ResteasyViolationExceptionImpl error = (ResteasyViolationExceptionImpl) rootCause;
            response.errors(error.getConstraintViolations().stream().collect(Collectors.toMap(this::getFieldPath, ConstraintViolation::getMessageTemplate)));
        }

        logger.error(exception.getMessage(), exception);

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(response.build()).build();
    }

    private Throwable getRootException(Throwable exception) {
        Objects.requireNonNull(exception);
        Throwable rootCause = exception;
        while (rootCause.getCause() != null && rootCause.getCause() != rootCause) {
            rootCause = rootCause.getCause();
        }
        return rootCause;
    }

    private String getFieldPath(ConstraintViolation<?> constraint) {
        StringBuilder path = new StringBuilder();
        Iterator<Node> iterator = constraint.getPropertyPath().iterator();
        iterator.next();
        while (iterator.hasNext()) {
            Node node = iterator.next();
            if (node.getName() != null) {
                path.append(node.getName());
                if(iterator.hasNext()) {
                    path.append(".");
                }
            }
        }
        return path.toString();
    }


}


