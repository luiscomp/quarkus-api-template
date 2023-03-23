package com.logicsoftware.services;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import com.logicsoftware.dtos.user.UserCreateDto;
import com.logicsoftware.dtos.user.UserDto;
import com.logicsoftware.dtos.user.UserFilterDto;

public interface UsersService {

    List<UserDto> findAll(UserFilterDto filterDto, Integer page, Integer size);

    Long count(UserFilterDto filterDto);

    UserDto find(Long id);

    UserDto create(UserCreateDto userDto);

    UserDto update(UserCreateDto userDto, Long id) throws IllegalAccessException, InvocationTargetException;

    void delete(Long id);

}