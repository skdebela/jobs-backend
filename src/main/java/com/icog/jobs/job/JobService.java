package com.icog.jobs.job;

import com.icog.jobs.company.models.Industry;
import com.icog.jobs.job.enums.ExperienceLevel;
import com.icog.jobs.job.enums.JobType;
import com.icog.jobs.job.enums.WorkMode;
import com.icog.jobs.company.CompanyRepository;
import com.icog.jobs.company.CompanyService;
import com.icog.jobs.company.models.Company;
import com.icog.jobs.job.dtos.CreateJobDto;
import com.icog.jobs.job.dtos.JobResponseDto;
import com.icog.jobs.job.dtos.UpdateJobDto;
import com.icog.jobs.job.models.Job;
import org.springframework.http.HttpStatus;
import jakarta.persistence.criteria.Predicate;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class JobService {

    private final JobMapper jobMapper;
    private final CompanyService companyService;
    JobRepository jobRepository;
    CompanyRepository companyRepository;

    public JobService(JobRepository jobRepository, JobMapper jobMapper, CompanyRepository companyRepository, CompanyService companyService) {
        this.jobRepository = jobRepository;
        this.jobMapper = jobMapper;
        this.companyRepository = companyRepository;
        this.companyService = companyService;
    }

    public JobResponseDto save(CreateJobDto createJobDto) {
        Company company = companyRepository.findById(createJobDto.getCompanyId()).get();

        Job job = jobMapper.mapFromCreate(createJobDto);
        job.setCompany(company);

        Job savedJob = jobRepository.save(job);
        return jobMapper.mapToResponse(savedJob);
    }

    public JobResponseDto createJob(CreateJobDto createJobDto) {
        Company company = companyRepository.findById(createJobDto.getCompanyId()).
                orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Company not found."));

        if (isDuplicate(createJobDto)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "A similar job already exists.");
        }

        Job job = jobMapper.mapFromCreate(createJobDto);
        Job savedJob = jobRepository.save(job);

        return jobMapper.mapToResponse(savedJob);
    }

    private boolean isDuplicate(CreateJobDto createJobDto) {
        // Adjust duplicate logic based on business rules
        return jobRepository.existsByTitleAndCompanyAndType(
                createJobDto.getTitle(),
                createJobDto.getCompanyId(),
                createJobDto.getType()
        );
    }

    public JobResponseDto update(UpdateJobDto updateJobDto, Integer id) {
        Assert.notNull(id, "The job ID must not be null");
        Company company = companyRepository.findById(updateJobDto.getCompanyId()).
                orElseThrow(() -> new IllegalArgumentException("Invalid Company Id: " + updateJobDto.getCompanyId()));

        Job existingJob = jobRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid Job Id: " + id));
        Job jobUpdate = jobMapper.mapFromUpdate(updateJobDto);
        jobUpdate.setCompany(company);
        jobUpdate.setId(id);

        Job savedJob = jobRepository.save(jobUpdate);
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
