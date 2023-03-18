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
import org.jboss.logging.Logger;

import com.logicsoftware.dtos.user.UserCreateDto;
import com.logicsoftware.dtos.user.UserDto;
import com.logicsoftware.dtos.user.UserFilterDto;
import com.logicsoftware.services.UsersService;
import com.logicsoftware.utils.enums.ResponseStatus;
import com.logicsoftware.utils.request.DataResponse;
import com.logicsoftware.utils.request.PageResponse;

import io.quarkus.arc.log.LoggerName;

@Tag(name = "Users")
@Path("/users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UsersResources {

    @Inject
    UsersService userService;
    
    @LoggerName("users-service")
    Logger logger;

    @POST
    @Path("/list")
    @Operation(description = "Use defined parameters to list specific page of users.", summary = "List a page of User")
    @Parameters({
            @Parameter(name = "page", description = "Page number", example = "1", required = true),
            @Parameter(name = "size", description = "Page size", example = "10", required = true)
    })
    public PageResponse<UserDto> list(UserFilterDto filter, @QueryParam("page") Integer page, @QueryParam("size") Integer size) {
        PageResponse.PageResponseBuilder<UserDto> response = PageResponse.builder();

        response.page(userService.findAll(filter, page, size));
        response.totalElements(userService.count(filter));
        response.pageSize(size);

        return response.build();
    }

    @GET
    @Path("/{id}")
    @Operation(description = "Pass id param to find an user", summary = "Get an User")
    @Parameters({
            @Parameter(name = "id", description = "User id", example = "1", required = true)
    })
    public DataResponse<UserDto> find(@PathParam("id") Long id) {
        DataResponse.DataResponseBuilder<UserDto> response = DataResponse.builder();
        response.data(userService.find(id));
        response.status(ResponseStatus.SUCCESS);
        return response.build();
    }

    @POST
    @Operation(description = "Create a new User", summary = "Create a new User")
    @Transactional
    public DataResponse<UserDto> create(@Valid UserCreateDto user) {
        DataResponse.DataResponseBuilder<UserDto> response = DataResponse.builder();
        response.data(userService.create(user));
        response.status(ResponseStatus.SUCCESS);
        return response.build();
    }

    @PUT
    @Path("/{id}")
    @Operation(description = "Update an User from id", summary = "Update an User")
    @Parameters({
            @Parameter(name = "id", description = "User id", example = "1", required = true)
    })
    @Transactional
    public DataResponse<UserDto> update(@Valid UserCreateDto user, @PathParam("id") Long id) throws IllegalAccessException, InvocationTargetException {
        DataResponse.DataResponseBuilder<UserDto> response = DataResponse.builder();
        response.data(userService.update(user, id));
        response.status(ResponseStatus.SUCCESS);
        return response.build();
    }

    @DELETE
    @Path("/{id}")
    @Operation(summary = "Delete an User", description = "Delete an User from id")
    @Parameters({
        @Parameter(name = "id", description = "User id", required = true, example = "1")
    })
    @Transactional
    public DataResponse<Void> delete(@PathParam("id") Long id) {
        DataResponse.DataResponseBuilder<Void> response = DataResponse.builder();
        userService.delete(id);
        response.status(ResponseStatus.SUCCESS);
        return response.build();
    }
}