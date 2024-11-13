package com.icog.jobs;

import com.icog.jobs.company.dtos.CompanyDto;
import com.icog.jobs.company.models.Company;
import com.icog.jobs.company.models.Industry;
import com.icog.jobs.job.enums.ExperienceLevel;
import com.icog.jobs.job.enums.JobStatus;
import com.icog.jobs.job.enums.JobType;
import com.icog.jobs.job.enums.WorkMode;
import com.icog.jobs.job.models.Job;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class TestDataUtil {
    public static Company createTestCompany() {
        return Company.builder()
                .name("Amazon")
                .industry(Industry.TECHNOLOGY)
                .website("https://www.amazon.com")
                .headquarters("California, USA")
                .build();
    }

    public static Company createTestCompany2() {
        return Company.builder()
                .name("Google")
                .industry(Industry.TECHNOLOGY)
                .website("https://www.google.com")
                .headquarters("New York, USA")
                .build();
    }

    public static Company createTestCompany3() {
        return Company.builder()
                .name("Apple")
                .industry(Industry.TECHNOLOGY)
                .website("https://www.apple.com")
                .headquarters("Boston, USA")
                .build();
    }

    public static Company createTestCompany4() {
        return Company.builder()
                .name("Queen's Supermarket")
                .industry(Industry.RETAIL)
                .website("https://www.queens.com")
                .headquarters("Addis Ababa, Ethiopia")
                .build();
    }

    public static CompanyDto createTestCompanyDto() {
        return CompanyDto.builder()
                .name("Amazon")
                .industry(Industry.TECHNOLOGY)
                .website("https://www.amazon.com")
                .headquarters("California, USA")
                .build();
    }

    public static CompanyDto createTestCompany2Dto() {
        return CompanyDto.builder()
                .name("Google")
                .industry(Industry.TECHNOLOGY)
                .website("https://www.google.com")
                .headquarters("New York, USA")
                .build();
    }

    public static Job createTestJob(final Company company) {
        return Job.builder()
                .title("Software Engineer")
                .company(company)
                .postedAt(LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS))
                .experienceLevel(ExperienceLevel.ENTRY_LEVEL)
                .type(JobType.FULL_TIME)
                .workMode(WorkMode.HYBRID)
                .status(JobStatus.ACTIVE)
                .description("Software Engineer with focus in System Engineering")
                .build();
    }

    public static Job createTestJob2(final Company company) {
        return Job.builder()
                .title("Accountant")
                .company(company)
                .postedAt(LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS))
                .experienceLevel(ExperienceLevel.ENTRY_LEVEL)
                .type(JobType.PART_TIME)
                .workMode(WorkMode.HYBRID)
                .status(JobStatus.ACTIVE)
                .description("Part time accountant needed in entry level experience.")
                .build();
    }
}
