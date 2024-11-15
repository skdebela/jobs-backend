package com.icog.jobs.application;

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

    @Autowired
    public ApplicationService(ApplicationRepository applicationRepository, JobRepository jobRepository) {
        this.applicationRepository = applicationRepository;
        this.jobRepository = jobRepository;
    }

    public Application save(Application application) {
        return applicationRepository.save(application);
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
                    return applicationRepository.save(application);
                }
        ).orElseThrow(() -> new NotFoundException("Application not found"));
    }
}
