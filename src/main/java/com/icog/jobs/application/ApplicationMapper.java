package com.icog.jobs.application;

import com.icog.jobs.application.dtos.ApplicationResponseDto;
import com.icog.jobs.application.dtos.CreateApplicationDto;
import com.icog.jobs.application.models.Application;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class ApplicationMapper{
    private ModelMapper modelMapper;

    public ApplicationMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public ApplicationResponseDto mapToResponse(Application application) {
        return modelMapper.map(application, ApplicationResponseDto.class);
    }

    public Application mapFromCreate(CreateApplicationDto createApplicationDto) {
        Application application = new Application();

        application.setApplicantName(createApplicationDto.getApplicantName());
        application.setApplicantEmail(createApplicationDto.getApplicantEmail());
        application.setApplicantPhone(createApplicationDto.getApplicantPhone());
        application.setApplicantAddress(createApplicationDto.getApplicantAddress());
        application.setApplicantBirthDate(createApplicationDto.getApplicantBirthDate());
        application.setApplicantGender(createApplicationDto.getApplicantGender());
        application.setApplicantEducationLevel(createApplicationDto.getApplicantEducationLevel());
        application.setApplicantExperienceLevel(createApplicationDto.getApplicantExperienceLevel());
        application.setCoverLetter(createApplicationDto.getCoverLetter());

        return application;
    }
}
