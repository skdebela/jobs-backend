package com.icog.jobs.application;

import com.icog.jobs.TestDataUtil;
import com.icog.jobs.application.models.Application;
import com.icog.jobs.company.CompanyRepository;
import com.icog.jobs.company.models.Company;
import com.icog.jobs.job.JobRepository;
import com.icog.jobs.job.enums.JobStatus;
import com.icog.jobs.job.models.Job;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ApplicationRepositoryTests {
    @Autowired
    ApplicationRepository underTest;
    @Autowired
    private JobRepository jobRepository;


    @Test
    public void testThatApplicationCanBeCreatedAndRecalled() {
        Company testCompany = TestDataUtil.createTestCompany();
        Job testJob = TestDataUtil.createTestJob(testCompany);
        jobRepository.save(testJob);
        Application testApplication = TestDataUtil.createTestApplication(testJob);
        underTest.save(testApplication);

        Optional<Application> result = underTest.findById(testApplication.getId());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(testApplication);

    }
    // TODO: testThatMultipleApplicationsCanBeCreatedAndRecalled
    // TODO: testThatApplicationCanBeUpdatedAndRecalled
    // TODO: testThatApplicationCanBeDeleted


}
