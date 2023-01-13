package com.logicsoftware.utils.mappers;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.modelmapper.ModelMapper;

@ApplicationScoped
public class GenericMapper implements IGenericMapper {

    @Inject
    ModelMapper mapper;

    @Override
    public <T> T toObject(Object obj, Class<T> clazz) {
        if(Objects.isNull(obj)) return null;
        return mapper.map(obj, clazz);
    }

    @Override
    public <T> List<T> toList(List<?> list, Class<T> clazz) {
        if(Objects.isNull(list) || list.isEmpty()) return Collections.emptyList();
        return list.stream().map(obj -> toObject(obj, clazz)).collect(Collectors.toList());
    }
    
}
