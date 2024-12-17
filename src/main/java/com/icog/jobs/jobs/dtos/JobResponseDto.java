package com.icog.jobs.jobs.dtos;

import com.icog.jobs.companies.models.Company;
import com.icog.jobs.jobs.enums.ExperienceLevel;
import com.icog.jobs.jobs.enums.JobStatus;
import com.icog.jobs.jobs.enums.JobType;
import com.icog.jobs.jobs.enums.WorkMode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class JobResponseDto {
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
