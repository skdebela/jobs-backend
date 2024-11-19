package com.icog.jobs.application;

import com.icog.jobs.application.dtos.ApplicationResponseDto;
import com.icog.jobs.application.dtos.CreateApplicationDto;
import com.icog.jobs.application.enums.ApplicationStatus;
import com.icog.jobs.application.models.Application;
import com.icog.jobs.job.JobRepository;
import com.icog.jobs.job.models.Job;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.util.List;
import java.util.Optional;

@Service
public class ApplicationService {
    private final ApplicationRepository applicationRepository;
    private final JobRepository jobRepository;
    private final ApplicationMapper applicationMapper;

    @Autowired
    public ApplicationService(ApplicationRepository applicationRepository, JobRepository jobRepository, ApplicationMapper applicationMapper) {
        this.applicationRepository = applicationRepository;
        this.jobRepository = jobRepository;
        this.applicationMapper = applicationMapper;
    }

    public ApplicationResponseDto save(CreateApplicationDto createApplicationDto, Integer jobId) {
        Job job = jobRepository.findById(jobId).get();

        Application application = applicationMapper.mapFromCreate(createApplicationDto);
        application.setJob(job);

        Application savedApplication = applicationRepository.save(application);
        return applicationMapper.mapToResponse(savedApplication);
    }


    public List<Application> findByJob(Job job) {
        return applicationRepository.findByJob(job);
    }

    public List<Application> findAll() {
        return applicationRepository.findAll();
    }

    public Optional<Application> findOne(Integer id) {
        return applicationRepository.findById(id);
    }

    public Application updateStatus(Integer applicationId, ApplicationStatus status) {
        Optional<Application> foundApplication = applicationRepository.findById(applicationId);
        return foundApplication.map(application -> {
                    application.setStatus(status);
                    return applicationRepository.save(application);
                }
        ).orElseThrow(() -> new NotFoundException("Application not found"));
    }
}
