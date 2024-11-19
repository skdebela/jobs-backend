package com.icog.jobs.job.dtos;

import com.icog.jobs.job.enums.ExperienceLevel;
import com.icog.jobs.job.enums.JobStatus;
import com.icog.jobs.job.enums.JobType;
import com.icog.jobs.job.enums.WorkMode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateJobDto {
    private Integer id;
    private String title;
    private Integer companyId;
    private ExperienceLevel experienceLevel;
    private JobType type;
    private WorkMode workMode;
    private JobStatus status;
    private String description;
}
