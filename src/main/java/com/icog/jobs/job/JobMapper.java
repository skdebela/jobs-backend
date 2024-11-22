package com.icog.jobs.job;

import com.icog.jobs.Mapper;
import com.icog.jobs.company.CompanyService;
import com.icog.jobs.company.models.Company;
import com.icog.jobs.job.dtos.CreateJobDto;
import com.icog.jobs.job.dtos.JobResponseDto;
import com.icog.jobs.job.dtos.UpdateJobDto;
import com.icog.jobs.job.models.Job;
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
        Company company = companyService.findOne(createJobDto.getCompanyId()).get();
        Job job = modelMapper.map(createJobDto, Job.class);
        job.setCompany(company);

        return job;
    }

    public Job mapFromUpdate(UpdateJobDto updateJobDto) {
        return modelMapper.map(updateJobDto, Job.class);
    }
}
