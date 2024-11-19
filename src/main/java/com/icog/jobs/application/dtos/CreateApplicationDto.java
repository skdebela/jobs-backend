package com.icog.jobs.application.dtos;

import com.icog.jobs.application.enums.EducationLevel;
import com.icog.jobs.application.enums.Gender;
import com.icog.jobs.job.enums.ExperienceLevel;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateApplicationDto {

    private Integer jobId;

    @NotBlank(message = "Applicant name must not be blank.")
    @Size(min = 2, max = 100, message = "Applicant name must be between 2 and 100 characters.")
    private String applicantName;

    @NotBlank(message = "Applicant email must not be blank.")
    @Email(message = "Applicant email must be a valid email address.")
    private String applicantEmail;

    @NotBlank(message = "Applicant phone must not be blank.")
    @Pattern(regexp = "^0[79]\\d{8}$", message = "Applicant phone must be a valid phone number.")
    private String applicantPhone;

    @NotBlank(message = "Applicant address must not be blank.")
    @Size(max = 255, message = "Applicant address must not exceed 255 characters.")
    private String applicantAddress;

    @NotNull(message = "Applicant birth date must not be null.")
    @Past(message = "Applicant birth date must be in the past.")
    private LocalDate applicantBirthDate;

    @NotNull(message = "Applicant gender must not be null.")
    private Gender applicantGender;

    @NotNull(message = "Applicant education level must not be null.")
    private EducationLevel applicantEducationLevel;

    @NotNull(message = "Applicant experience level must not be null.")
    private ExperienceLevel applicantExperienceLevel;

    @Size(max = 2000, message = "Cover letter must not exceed 2000 characters.")
    private String coverLetter;

    //TODO: cvUrl
    //TODO: otherDocumentsUrl
}
