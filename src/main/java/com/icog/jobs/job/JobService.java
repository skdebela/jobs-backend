package com.icog.jobs.job;

import ch.qos.logback.core.helpers.CyclicBuffer;
import com.icog.jobs.company.models.Industry;
import com.icog.jobs.job.enums.ExperienceLevel;
import com.icog.jobs.job.enums.JobType;
import com.icog.jobs.job.enums.WorkMode;
import com.icog.jobs.job.models.Job;
import jakarta.persistence.criteria.Predicate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    public List<Job> searchAndFilter(Industry industry, ExperienceLevel experienceLevel, JobType type, WorkMode workMode, String query) {
        return jobRepository.findAll((root, cq, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            var companyJoin = root.join("company");

            if (industry != null) {
                predicates.add(cb.equal(companyJoin.get("industry"), industry));
            }
            if (experienceLevel != null) {
                predicates.add(cb.equal(root.get("experienceLevel"), experienceLevel));
            }
            if (type != null) {
                predicates.add(cb.equal(root.get("type"), type));
            }
            if (workMode != null) {
                predicates.add(cb.equal(root.get("workMode"), workMode));
            }

            if (query != null) {
                String likeQuery = "%" + query.toLowerCase() + "%";
                Predicate titleMatch = cb.like(cb.lower(root.get("title")), likeQuery);
                Predicate companyNameMatch = cb.like(cb.lower(companyJoin.get("name")), likeQuery);
                Predicate addressMatch = cb.like(cb.lower(companyJoin.get("headquarters")), likeQuery);
                Predicate descriptionMatch = cb.like(cb.lower(root.get("description")), likeQuery);
                Predicate experienceLevelMatch = cb.like(cb.lower(root.get("experienceLevel")), likeQuery);
                Predicate typeMatch = cb.like(cb.lower(root.get("type")), likeQuery);
                Predicate workModeMatch = cb.like(cb.lower(root.get("workMode")), likeQuery);

                predicates.add(cb.or(titleMatch, companyNameMatch, addressMatch, descriptionMatch, experienceLevelMatch, typeMatch, workModeMatch));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        });
    }
}
