package com.logicsoftware.services;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import com.logicsoftware.dtos.user.UserCreateDto;
import com.logicsoftware.dtos.user.UserFilterDto;

public interface UsersService {

    List<Object> findAll(UserFilterDto filterDto, Integer page, Integer size);

    Long count(UserFilterDto filterDto);

    Object find(Long id);

    Object create(UserCreateDto userDto);

    Object update(UserCreateDto userDto, Long id) throws IllegalAccessException, InvocationTargetException;

    void delete(Long id);

}