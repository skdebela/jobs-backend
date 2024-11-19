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

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateJobDto {
    private Integer id;
    private String title;
    private Integer companyId;
    private ExperienceLevel experienceLevel;
    private JobType type;
    private WorkMode workMode;
    private String description;

}
