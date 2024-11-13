package com.icog.jobs.job;

import com.icog.jobs.Mapper;
import com.icog.jobs.job.dtos.JobDto;
import com.icog.jobs.job.models.Job;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class JobMapper implements Mapper<Job, JobDto> {
    private ModelMapper modelMapper;

    public JobMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public JobDto mapTo(Job job) {
        return modelMapper.map(job, JobDto.class);
    }

    @Override
    public Job mapFrom(JobDto jobDto) {
        return modelMapper.map(jobDto, Job.class);
    }
}
