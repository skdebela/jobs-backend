package com.icog.jobs.jobs;

import com.icog.jobs.companies.CompanyService;
import com.icog.jobs.jobs.dtos.CreateJobDto;
import com.icog.jobs.jobs.dtos.JobResponseDto;
import com.icog.jobs.jobs.dtos.UpdateJobDto;
import com.icog.jobs.jobs.models.Job;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class JobMapper{
    private final CompanyService companyService;
    private ModelMapper modelMapper;

    @Autowired
    public JobMapper(ModelMapper modelMapper, CompanyService companyService) {
        this.modelMapper = modelMapper;
        this.companyService = companyService;
    }

    public JobResponseDto mapToResponse(Job job) {
        return modelMapper.map(job, JobResponseDto.class);
    }

    public Job mapFromCreate(CreateJobDto createJobDto) {
        Job job = new Job();

        job.setTitle(createJobDto.getTitle());
        job.setExperienceLevel(createJobDto.getExperienceLevel());
        job.setType(createJobDto.getType());
        job.setWorkMode(createJobDto.getWorkMode());
        job.setDescription(createJobDto.getDescription());

        return job;
    }

    public Job mapFromUpdate(UpdateJobDto updateJobDto) {
        return modelMapper.map(updateJobDto, Job.class);
    }
}
