package com.icog.jobs.job.dtos;

import com.icog.jobs.job.enums.ExperienceLevel;
import com.icog.jobs.job.enums.JobType;
import com.icog.jobs.job.enums.WorkMode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateJobDto {
    @NotBlank(message = "Title must not be blank.")
    @Size(min = 2, max = 100, message = "Title must not exceed 100 characters.")
    private String title;

    @NotNull(message = "Company ID must not be null.")
    private Integer companyId;

    @NotNull(message = "Experience level must not be null.")
    private ExperienceLevel experienceLevel;

    @NotNull(message = "Job type must not be null.")
    private JobType type;

    @NotNull(message = "Work mode must not be null.")
    private WorkMode workMode;

    @Size(max = 1000, message = "Description must not exceed 1000 characters.")
    private String description;
}
