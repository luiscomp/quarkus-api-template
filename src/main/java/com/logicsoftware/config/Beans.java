package com.logicsoftware.config;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

import com.logicsoftware.utils.i18n.Messages;

import io.quarkus.vertx.http.runtime.CurrentVertxRequest;

@ApplicationScoped
public class Beans {

    @Inject
    CurrentVertxRequest request;
    
    @Produces
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        return modelMapper;
    }

    @Produces
    public Messages messages() {
        return Messages.builder().defaultLang("en").request(request).build();
    }
}
