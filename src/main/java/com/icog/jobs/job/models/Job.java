package com.icog.jobs.job.models;

import com.icog.jobs.company.models.Company;
import com.icog.jobs.job.enums.ExperienceLevel;
import com.icog.jobs.job.enums.JobStatus;
import com.icog.jobs.job.enums.JobType;
import com.icog.jobs.job.enums.WorkMode;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "job")
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String title;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    private LocalDateTime postedAt = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    private ExperienceLevel experienceLevel;

    @Enumerated(EnumType.STRING)
    private JobType type;

    @Enumerated(EnumType.STRING)
    private WorkMode workMode;

    @Enumerated(EnumType.STRING)
    private JobStatus status = JobStatus.ACTIVE;

    private String description;
}
