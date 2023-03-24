package com.logicsoftware.services.v1;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.NotFoundException;

import org.apache.commons.beanutils.BeanUtils;
import org.jboss.logging.Logger;

import com.logicsoftware.dtos.user.UserCreateDto;
import com.logicsoftware.dtos.user.UserDto;
import com.logicsoftware.dtos.user.UserFilterDto;
import com.logicsoftware.models.User;
import com.logicsoftware.repositories.UsersRepository;
import com.logicsoftware.services.UserService;
import com.logicsoftware.utils.i18n.Messages;
import com.logicsoftware.utils.mappers.GenericMapper;

import io.quarkus.arc.Unremovable;
import io.quarkus.arc.log.LoggerName;
@Unremovable
@ApplicationScoped
public class UserServiceV1 implements UserService {

    @LoggerName("users-service")
    Logger logger;

    @Inject
    GenericMapper mapper;

    @Inject
    Messages message;

    @Inject
    UsersRepository usersRepository;

    private User getUser(Long id) {
        Optional<User> userFound =  usersRepository.find(id);
        return userFound.orElseThrow(() -> new NotFoundException(message.getMessage("user.not.found", id)));
    }

    @Override
    public List<Object> findAll(UserFilterDto filterDto, Integer page, Integer size) {
        return mapper.toList(usersRepository.findPage(filterDto, page, size), Object.class);
    }

    @Override
    public Long count(UserFilterDto filterDto) {
        return usersRepository.count(filterDto);
    }

    @Override
    public UserDto find(Long id) {
        User userFound = getUser(id);
        return mapper.toObject(userFound, UserDto.class);
    }

    @Override
    public UserDto create(UserCreateDto userDto) {
        User newUser = mapper.toObject(userDto, User.class);
        logger.info("[+] Creating user: " + newUser.toString());
        return mapper.toObject(usersRepository.create(newUser), UserDto.class);
    }

    @Override
    public UserDto update(UserCreateDto userDto, Long id) throws IllegalAccessException, InvocationTargetException {
        User userFound =  getUser(id);
        BeanUtils.copyProperties(userFound, userDto);
        var updatedUser = usersRepository.update(userFound);
        logger.info("[+] Updated user: " + updatedUser.toString());
        return  mapper.toObject(updatedUser, UserDto.class);
    }

    @Override
    public void delete(Long id) {
        getUser(id);
        usersRepository.delete(id);
    }
}
