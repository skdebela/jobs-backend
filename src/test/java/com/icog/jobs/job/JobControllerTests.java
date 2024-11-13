package com.icog.jobs.job;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.icog.jobs.TestDataUtil;
import com.icog.jobs.company.models.Company;
import com.icog.jobs.job.models.Job;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class JobControllerTests {
    private final JobService jobService;
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    @Autowired
    public JobControllerTests(JobService jobService, MockMvc mockMvc, ObjectMapper objectMapper) {
        this.jobService = jobService;
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }

    @Test
    public void testThatCreateJobsReturnsHttp201Created() throws Exception {
        Company testCompany = TestDataUtil.createTestCompany();
        Job testJob = TestDataUtil.createTestJob(testCompany);
        String testJobJson = objectMapper.writeValueAsString(testJob);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/jobs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(testJobJson)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        );
    }

    @Test
    public void testThatCreateJobReturnsJob() throws Exception {
        Company testCompany = TestDataUtil.createTestCompany();
        Job testJob = TestDataUtil.createTestJob(testCompany);
        String testJobJson = objectMapper.writeValueAsString(testJob);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/jobs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(testJobJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").isNumber()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.title").value(testJob.getTitle())
//        ).andExpect(
//                MockMvcResultMatchers.jsonPath("$.postedAt").value(job.getPostedAt())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.experienceLevel").value(testJob.getExperienceLevel().toString())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.type").value(testJob.getType().toString())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.workMode").value(testJob.getWorkMode().toString())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.status").value(testJob.getStatus().toString())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.description").value(testJob.getDescription())
        );
    }


    @Test
    public void testThatGetJobsReturnsHttp200Ok() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/jobs")
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatGetJobsReturnsListOfJobs() throws Exception {
        Company company = TestDataUtil.createTestCompany();
        Job job = TestDataUtil.createTestJob(company);
        jobService.save(job);
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/jobs")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.[0].id").value(job.getId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.[0].title").value(job.getTitle())
//        ).andExpect(
//                MockMvcResultMatchers.jsonPath("$.[0].postedAt").value(job.getPostedAt())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.[0].experienceLevel").value(job.getExperienceLevel().toString())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.[0].type").value(job.getType().toString())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.[0].workMode").value(job.getWorkMode().toString())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.[0].status").value(job.getStatus().toString())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.[0].description").value(job.getDescription())
        );
    }

    @Test
    public void testThatGetJobReturn200OkWhenJobExists() throws Exception {
        Company company = TestDataUtil.createTestCompany();
        Job job = TestDataUtil.createTestJob(company);
        jobService.save(job);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/jobs/" + job.getId())
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatGetJobReturn404NotFoundWhenJobDoesNotExist() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/jobs/" + 1)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    public void testThatGetJobReturnsJobWhenJobExists() throws Exception {
        Company company = TestDataUtil.createTestCompany();
        Job job = TestDataUtil.createTestJob(company);
        jobService.save(job);
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/jobs/" + job.getId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").value(job.getId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.title").value(job.getTitle())
//        ).andExpect(
//                MockMvcResultMatchers.jsonPath("$.postedAt").value(job.getPostedAt())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.experienceLevel").value(job.getExperienceLevel().toString())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.type").value(job.getType().toString())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.workMode").value(job.getWorkMode().toString())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.status").value(job.getStatus().toString())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.description").value(job.getDescription())
        );
    }

    // update
    @Test
    public void testThatUpdateJobsReturnsHttp201Created() throws Exception {
        Company testCompany1 = TestDataUtil.createTestCompany();
        Job testJob1 = TestDataUtil.createTestJob(testCompany1);
        jobService.save(testJob1);

        Company testCompany2 = TestDataUtil.createTestCompany2();
        Job testJob2 = TestDataUtil.createTestJob2(testCompany2);
        String testJobJson2 = objectMapper.writeValueAsString(testJob2);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/api/jobs/" + testJob1.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(testJobJson2)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatUpdateJobReturn404NotFoundWhenJobDoesNotExist() throws Exception {
        Company testCompany2 = TestDataUtil.createTestCompany2();
        Job testJob2 = TestDataUtil.createTestJob2(testCompany2);
        String testJobJson2 = objectMapper.writeValueAsString(testJob2);
        mockMvc.perform(
                MockMvcRequestBuilders.put("/api/jobs/" + 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(testJobJson2)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    public void testThatUpdateJobReturnsJob() throws Exception {
        Company testCompany1 = TestDataUtil.createTestCompany();
        Job testJob1 = TestDataUtil.createTestJob(testCompany1);
        Job savedTestJob1 = jobService.save(testJob1);

        Company testCompany2 = TestDataUtil.createTestCompany2();
        Job testJob2 = TestDataUtil.createTestJob2(testCompany2);
        String testJobJson2 = objectMapper.writeValueAsString(testJob2);
        mockMvc.perform(
                MockMvcRequestBuilders.put("/api/jobs/" + savedTestJob1.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(testJobJson2)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").value(savedTestJob1.getId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.title").value(testJob2.getTitle())
//        ).andExpect(
//                MockMvcResultMatchers.jsonPath("$.postedAt").value(job.getPostedAt())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.experienceLevel").value(testJob2.getExperienceLevel().toString())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.type").value(testJob2.getType().toString())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.workMode").value(testJob2.getWorkMode().toString())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.status").value(testJob2.getStatus().toString())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.description").value(testJob2.getDescription())
        );
    }

    @Test
    public void testThatDeleteJobReturnsHttpStatus204NoContentForNonExistingJob() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/jobs/1")
        ).andExpect(
                MockMvcResultMatchers.status().isNoContent()
        );
    }

    @Test
    public void testThatDeleteJobReturnsHttpStatus204NoContentForExistingJob() throws Exception {
        Company testCompany = TestDataUtil.createTestCompany();
        Job testJob = TestDataUtil.createTestJob(testCompany);
        Job savedJob = jobService.save(testJob);

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/jobs/" + savedJob.getId())
        ).andExpect(
                MockMvcResultMatchers.status().isNoContent()
        );
    }
}
