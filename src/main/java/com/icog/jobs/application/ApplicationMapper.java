package com.icog.jobs.application;

import com.icog.jobs.Mapper;
import com.icog.jobs.application.dtos.ApplicationDto;
import com.icog.jobs.application.models.Application;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class ApplicationMapper implements Mapper<Application, ApplicationDto> {
    private ModelMapper modelMapper;

    public ApplicationMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public ApplicationDto mapTo(Application application) {
        return modelMapper.map(application, ApplicationDto.class);
    }

    @Override
    public Application mapFrom(ApplicationDto applicationDto) {
        return modelMapper.map(applicationDto, Application.class);
    }
}
