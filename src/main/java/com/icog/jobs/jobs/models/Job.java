package com.icog.jobs.jobs.models;

import com.icog.jobs.companies.models.Company;
import com.icog.jobs.jobs.enums.ExperienceLevel;
import com.icog.jobs.jobs.enums.JobStatus;
import com.icog.jobs.jobs.enums.JobType;
import com.icog.jobs.jobs.enums.WorkMode;
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

    @Builder.Default
    private LocalDateTime postedAt = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    private ExperienceLevel experienceLevel;

    @Enumerated(EnumType.STRING)
    private JobType type;

    @Enumerated(EnumType.STRING)
    private WorkMode workMode;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private JobStatus status = JobStatus.ACTIVE;

    private String description;
}
