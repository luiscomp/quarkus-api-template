package com.logicsoftware.config;

import javax.enterprise.inject.Produces;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

public class Beans {
    
    @Produces
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        return modelMapper;
    }
}
