package com.icog.jobs.job.dtos;

import com.icog.jobs.company.models.Company;
import com.icog.jobs.job.enums.ExperienceLevel;
import com.icog.jobs.job.enums.JobStatus;
import com.icog.jobs.job.enums.JobType;
import com.icog.jobs.job.enums.WorkMode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class JobDto {
    private Integer id;
    private String title;
    private Company company;
    private LocalDateTime postedAt;
    private ExperienceLevel experienceLevel;
    private JobType type;
    private WorkMode workMode;
    private JobStatus status;
    private String description;
}
