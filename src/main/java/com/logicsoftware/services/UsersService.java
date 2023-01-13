package com.logicsoftware.services;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.NotFoundException;

import org.apache.commons.beanutils.BeanUtils;

import com.logicsoftware.dtos.user.UserCreateDto;
import com.logicsoftware.dtos.user.UserDto;
import com.logicsoftware.dtos.user.UserFilterDto;
import com.logicsoftware.models.User;
import com.logicsoftware.repositories.UsersRepository;
import com.logicsoftware.utils.mappers.GenericMapper;

@ApplicationScoped
public class UsersService {

    @Inject
    UsersRepository usersRepository;

    @Inject
    GenericMapper mapper;

    private User getUser(Long id) {
        Optional<User> userFound =  usersRepository.find(id);
        userFound.orElseThrow(() -> new NotFoundException("User not found"));
        return userFound.get();
    }

    public List<UserDto> findAll(UserFilterDto filterDto, Integer page, Integer size) {
        return mapper.toList(usersRepository.findPage(filterDto, page, size), UserDto.class);
    }

    public Long count(UserFilterDto filterDto) {
        return usersRepository.count(filterDto);
    }

    public UserDto find(Long id) {
        User userFound = getUser(id);
        return mapper.toObject(userFound, UserDto.class);
    }

    public UserDto create(UserCreateDto userDto) {
        User newUser = mapper.toObject(userDto, User.class);
        return mapper.toObject(usersRepository.create(newUser), UserDto.class);
    }

    public UserDto update(UserCreateDto userDto, Long id) throws IllegalAccessException, InvocationTargetException {
        User userFound =  getUser(id);
        BeanUtils.copyProperties(userFound, userDto);
        return  mapper.toObject(usersRepository.update(userFound), UserDto.class);
    }

    public void delete(Long id) {
        getUser(id);
        usersRepository.delete(id);
    }
}
