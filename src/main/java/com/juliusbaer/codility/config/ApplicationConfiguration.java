package com.juliusbaer.codility.config;

import com.juliusbaer.codility.api.domain.ApiUser;
import com.juliusbaer.codility.repository.entity.User;
import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

@Configuration
public class ApplicationConfiguration {

    @Bean
    public ModelMapper configureModelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STANDARD);
        modelMapper.getConfiguration().setAmbiguityIgnored(true);
        modelMapper.addConverter(LOCALDATE_CONVERTER);

        modelMapper.typeMap(ApiUser.class, User.class).addMappings(mapper -> {
            mapper.map(src -> src.getUserId(),
                    (destination, value) -> destination.setId((UUID) value));
        });

        modelMapper.typeMap(User.class, ApiUser.class).addMappings(mapper -> {
            mapper.map(src -> src.getId(),
                    (destination, value) -> destination.setUserId((UUID) value));
        });

        return modelMapper;
    }

    private static final Converter<LocalDateTime, Instant> LOCALDATE_CONVERTER = new AbstractConverter<LocalDateTime, Instant>() {
        @Override
        protected Instant convert(LocalDateTime source) {
            return source.toInstant(ZoneOffset.UTC);
        }
    };
}