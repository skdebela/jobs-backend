package com.icog.jobs.applications.models;

import com.icog.jobs.applications.enums.ApplicationStatus;
import com.icog.jobs.applications.enums.EducationLevel;
import com.icog.jobs.applications.enums.Gender;
import com.icog.jobs.jobs.enums.ExperienceLevel;
import com.icog.jobs.jobs.models.Job;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "application")
public class Application {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "job_id")
    private Job job;

    private String applicantName;

    private String applicantEmail;

    private String applicantPhone;

    private String applicantAddress;

    private LocalDate applicantBirthDate;

    @Enumerated(EnumType.STRING)
    private Gender applicantGender;

    @Enumerated(EnumType.STRING)
    private EducationLevel applicantEducationLevel;

    @Enumerated(EnumType.STRING)
    private ExperienceLevel applicantExperienceLevel;


    private String coverLetter;

    @Enumerated(EnumType.STRING)
    private ApplicationStatus status = ApplicationStatus.SUBMITTED;

    //TODO: cvUrl
    //TODO: otherDocumentsUrl

}
