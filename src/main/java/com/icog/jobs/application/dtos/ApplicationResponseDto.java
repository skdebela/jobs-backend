package com.icog.jobs.application.dtos;

import com.icog.jobs.application.enums.ApplicationStatus;
import com.icog.jobs.application.enums.EducationLevel;
import com.icog.jobs.application.enums.Gender;
import com.icog.jobs.job.enums.ExperienceLevel;
import com.icog.jobs.job.models.Job;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationResponseDto {
    private Integer id;
    private Job job;
    private String applicantName;
    private String applicantEmail;
    private String applicantPhone;
    private String applicantAddress;
    private LocalDate applicantBirthDate;
    private Gender applicantGender;
    private EducationLevel applicantEducationLevel;
    private ExperienceLevel applicantExperienceLevel;
    private String coverLetter;
    private ApplicationStatus status;

    //TODO: cvUrl
    //TODO: otherDocumentsUrl
}
