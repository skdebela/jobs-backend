package com.icog.jobs.jobs.dtos;

import com.icog.jobs.jobs.enums.ExperienceLevel;
import com.icog.jobs.jobs.enums.JobStatus;
import com.icog.jobs.jobs.enums.JobType;
import com.icog.jobs.jobs.enums.WorkMode;
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
public class UpdateJobDto {

    Integer id;

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

    @NotNull(message = "Job status must not be null.")
    private JobStatus status;

    @Size(max = 1000, message = "Description must not exceed 1000 characters.")
    private String description;
}
