package com.icog.jobs.job;

import com.icog.jobs.company.CompanyRepository;
import com.icog.jobs.company.models.Company;
import com.icog.jobs.job.dtos.CreateJobDto;
import com.icog.jobs.job.dtos.JobResponseDto;
import com.icog.jobs.job.dtos.UpdateJobDto;
import com.icog.jobs.job.models.Job;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;

@Service
public class JobService {

    private final JobMapper jobMapper;
    JobRepository jobRepository;
    CompanyRepository companyRepository;

    public JobService(JobRepository jobRepository, JobMapper jobMapper, CompanyRepository companyRepository) {
        this.jobRepository = jobRepository;
        this.jobMapper = jobMapper;
        this.companyRepository = companyRepository;
    }

    public JobResponseDto save(CreateJobDto createJobDto) {
        Company company = companyRepository.findById(createJobDto.getCompanyId()).
                orElseThrow(() -> new IllegalArgumentException("Invalid Company Id: " + createJobDto.getCompanyId()));

        Job job = jobMapper.mapFromCreate(createJobDto);
        job.setCompany(company);

        Job savedJob = jobRepository.save(job);
        return jobMapper.mapToResponse(savedJob);
    }

    public JobResponseDto update(UpdateJobDto updateJobDto, Integer id) {
        Assert.notNull(id, "The job ID must not be null");
        Company company = companyRepository.findById(updateJobDto.getCompanyId()).
                orElseThrow(() -> new IllegalArgumentException("Invalid Company Id: " + updateJobDto.getCompanyId()));

        Job existingJob = jobRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid Job Id: " + id));
        existingJob.setTitle(updateJobDto.getTitle());
        existingJob.setCompany(company);
        existingJob.setType(updateJobDto.getType());
        existingJob.setExperienceLevel(updateJobDto.getExperienceLevel());
        existingJob.setWorkMode(updateJobDto.getWorkMode());
        existingJob.setStatus(updateJobDto.getStatus());
        existingJob.setDescription(updateJobDto.getDescription());

        Job savedJob = jobRepository.save(existingJob);
        return jobMapper.mapToResponse(savedJob);
    }

    public List<Job> findAll() {
        return jobRepository.findAll();
    }

    public Optional<Job> findOne(Integer id) {
        return jobRepository.findById(id);
    }

    public boolean isExisting(Integer id) {
        return jobRepository.existsById(id);
    }

    public void delete(Integer id) {
        jobRepository.deleteById(id);
    }
}
