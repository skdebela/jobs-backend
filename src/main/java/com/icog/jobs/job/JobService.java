package com.icog.jobs.job;

import com.icog.jobs.job.models.Job;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class JobService {

    JobRepository jobRepository;

    public JobService(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    public Job save(Job job) {
        return jobRepository.save(job);
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
