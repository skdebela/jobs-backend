package com.icog.jobs.job;

import com.icog.jobs.Mapper;
import com.icog.jobs.job.dtos.CreateJobDto;
import com.icog.jobs.job.dtos.JobResponseDto;
import com.icog.jobs.job.dtos.UpdateJobDto;
import com.icog.jobs.job.models.Job;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class JobMapper{
    private ModelMapper modelMapper;

    public JobMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public JobResponseDto mapToResponse(Job job) {
        return modelMapper.map(job, JobResponseDto.class);
    }

    public Job mapFromCreate(CreateJobDto createJobDto) {
        return modelMapper.map(createJobDto, Job.class);
    }

    public Job mapFromUpdate(UpdateJobDto updateJobDto) {
        return modelMapper.map(updateJobDto, Job.class);
    }
}
