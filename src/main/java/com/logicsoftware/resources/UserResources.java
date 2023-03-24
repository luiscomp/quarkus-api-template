package com.logicsoftware.resources;

import java.lang.reflect.InvocationTargetException;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameters;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import com.logicsoftware.dtos.user.UserCreateDto;
import com.logicsoftware.dtos.user.UserFilterDto;
import com.logicsoftware.exceptions.ResourceNotFoundException;
import com.logicsoftware.services.UserService;
import com.logicsoftware.utils.BeanGenerator;
import com.logicsoftware.utils.enums.AppStatus;
import com.logicsoftware.utils.i18n.Messages;
import com.logicsoftware.utils.request.DataResponse;
import com.logicsoftware.utils.request.PageResponse;

@Tag(name = "Users")
@Path("/user/v{version}")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserResources {

    @Inject
    BeanGenerator beanGenerator;

    @Inject
    Messages message;
    

    @POST
    @Path("/list")
    @Operation(description = "Use defined parameters to list specific page of users.", summary = "List a page of User")
    @Parameters({
        @Parameter(name = "page", description = "Page number", example = "1", required = true),
        @Parameter(name = "size", description = "Page size", example = "10", required = true),
        @Parameter(name = "version", description = "Version of the API", required = true, example = "1")
    })
    public PageResponse<Object> list(UserFilterDto filter, @QueryParam("page") Integer page, @QueryParam("size") Integer size, @PathParam("version") String version) {
        UserService userService = beanGenerator.getInstance(UserService.class, version)
                .orElseThrow(
                    () -> new ResourceNotFoundException(message.getMessage("api.resource.not.found", version)));
        
        PageResponse.PageResponseBuilder<Object> response = PageResponse.builder();
        response.page(userService.findAll(filter, page, size));
        response.totalElements(userService.count(filter));
        response.pageSize(size);

        return response.build();
    }

    @GET
    @Path("/{id}")
    @Operation(description = "Pass id param to find an user", summary = "Get an User")
    @Parameters({
        @Parameter(name = "id", description = "User id", example = "1", required = true),
        @Parameter(name = "version", description = "Version of the API", required = true, example = "1")
    })
    public DataResponse<Object> find(@PathParam("id") Long id, @PathParam("version") String version) {
        UserService userService = beanGenerator.getInstance(UserService.class, version)
                .orElseThrow(
                    () -> new ResourceNotFoundException(message.getMessage("api.resource.not.found", version)));

        DataResponse.DataResponseBuilder<Object> response = DataResponse.builder();
        response.data(userService.find(id));
        response.status(AppStatus.SUCCESS);
        return response.build();
    }

    @POST
    @Operation(description = "Create a new User", summary = "Create a new User")
    @Parameters({
        @Parameter(name = "version", description = "Version of the API", required = true, example = "1")
    })
    @Transactional
    public DataResponse<Object> create(@Valid UserCreateDto user, @PathParam("version") String version) {
        UserService userService = beanGenerator.getInstance(UserService.class, version)
                .orElseThrow(
                    () -> new ResourceNotFoundException(message.getMessage("api.resource.not.found", version)));

        DataResponse.DataResponseBuilder<Object> response = DataResponse.builder();
        response.data(userService.create(user));
        response.status(AppStatus.SUCCESS);
        return response.build();
    }

    @PUT
    @Path("/{id}")
    @Operation(description = "Update an User from id", summary = "Update an User")
    @Parameters({
        @Parameter(name = "id", description = "User id", example = "1", required = true),
        @Parameter(name = "version", description = "Version of the API", required = true, example = "1")
    })
    @Transactional
    public DataResponse<Object> update(@Valid UserCreateDto user, @PathParam("id") Long id, @PathParam("version") String version) throws IllegalAccessException, InvocationTargetException {
        UserService userService = beanGenerator.getInstance(UserService.class, version)
                .orElseThrow(
                    () -> new ResourceNotFoundException(message.getMessage("api.resource.not.found", version)));

        DataResponse.DataResponseBuilder<Object> response = DataResponse.builder();
        response.data(userService.update(user, id));
        response.status(AppStatus.SUCCESS);
        return response.build();
    }

    @DELETE
    @Path("/{id}")
    @Operation(summary = "Delete an User", description = "Delete an User from id")
    @Parameters({
        @Parameter(name = "id", description = "User id", required = true, example = "1"),
        @Parameter(name = "version", description = "Version of the API", required = true, example = "1")
    })
    @Transactional
    public DataResponse<Void> delete(@PathParam("id") Long id, @PathParam("version") String version) {
        UserService userService = beanGenerator.getInstance(UserService.class, version)
                .orElseThrow(
                    () -> new ResourceNotFoundException(message.getMessage("api.resource.not.found", version)));

        DataResponse.DataResponseBuilder<Void> response = DataResponse.builder();
        userService.delete(id);
        response.status(AppStatus.SUCCESS);
        return response.build();
    }
}