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

    public List<Job> searchAndFilter(List<Industry> industries, List<ExperienceLevel> experienceLevels, List<JobType> types, List<WorkMode> workModes, String query) {
        return jobRepository.findAll((root, cq, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            var companyJoin = root.join("company");

            if (industries != null && !industries.isEmpty()) {
                predicates.add(companyJoin.get("industry").in(industries));
            }
            if (experienceLevels != null && !experienceLevels.isEmpty()) {
                predicates.add(root.get("experienceLevel").in(experienceLevels));
            }
            if (types != null && !types.isEmpty()) {
                predicates.add(root.get("type").in(types));
            }
            if (workModes != null && !workModes.isEmpty()) {
                predicates.add(root.get("workMode").in(workModes));
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
