package com.icog.jobs.job;

import com.icog.jobs.TestDataUtil;
import com.icog.jobs.company.models.Company;
import com.icog.jobs.job.enums.JobStatus;
import com.icog.jobs.job.models.Job;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
public class JobRepositoryTests {
    private JobRepository underTest;

    @Autowired
    public JobRepositoryTests(JobRepository jobRepository) {
        this.underTest = jobRepository;
    }

    @Test
    public void testThatJobCanBeCreatedAndRecalled() {
        Company company = TestDataUtil.createTestCompany();
        Job job = TestDataUtil.createTestJob(company);
        underTest.save(job);

        Optional<Job> result = underTest.findById(job.getId());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(job);
    }

    @Test
    public void testThatMultipleJobsCanBeCreatedAndRecalled() {
        Company company1 = TestDataUtil.createTestCompany();
        Job job1 = TestDataUtil.createTestJob(company1);
        underTest.save(job1);

        Company company2 = TestDataUtil.createTestCompany();
        Job job2 = TestDataUtil.createTestJob2(company2);
        underTest.save(job2);

        Iterable<Job> result = underTest.findAll();
        assertThat(result)
                .hasSize(2)
                .containsExactly(job1, job2);
    }

    @Test
    public void testThatJobCanBeUpdatedAndRecalled() {
        Company company = TestDataUtil.createTestCompany();
        Job job = TestDataUtil.createTestJob(company);
        underTest.save(job);
        job.setStatus(JobStatus.CLOSED);
        underTest.save(job);

        Optional<Job> result = underTest.findById(job.getId());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(job);
    }

    @Test
    public void testThatJobCanBeDeleted() {
        Company company = TestDataUtil.createTestCompany();
        Job job = TestDataUtil.createTestJob(company);
        underTest.save(job);

        underTest.delete(job);
        Optional<Job> result = underTest.findById(job.getId());
        assertThat(result).isEmpty();
    }


}
