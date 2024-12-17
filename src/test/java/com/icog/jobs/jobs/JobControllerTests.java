package com.icog.jobs.jobs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.icog.jobs.TestDataUtil;
import com.icog.jobs.companies.CompanyService;
import com.icog.jobs.companies.dtos.CompanyDto;
import com.icog.jobs.companies.dtos.CreateUpdateCompanyDto;
import com.icog.jobs.jobs.dtos.CreateJobDto;
import com.icog.jobs.jobs.dtos.JobResponseDto;
import com.icog.jobs.jobs.dtos.UpdateJobDto;
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
    private final CompanyService companyService;

    @Autowired
    public JobControllerTests(JobService jobService, MockMvc mockMvc, ObjectMapper objectMapper, CompanyService companyService) {
        this.jobService = jobService;
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
        this.companyService = companyService;
    }

    @Test
    public void testThatCreateJobsReturnsHttp201Created() throws Exception {
        CreateUpdateCompanyDto testCreateUpdateCompanyDto = TestDataUtil.createTestCompanyDto();
        CompanyDto testCompanyDto = companyService.save(testCreateUpdateCompanyDto);

        CreateJobDto testJob = TestDataUtil.createTestJobDto(testCompanyDto);
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
        CreateUpdateCompanyDto testCreateUpdateCompanyDto = TestDataUtil.createTestCompanyDto();
        CompanyDto testCompany = companyService.save(testCreateUpdateCompanyDto);

        CreateJobDto testJob = TestDataUtil.createTestJobDto(testCompany);
        String testJobJson = objectMapper.writeValueAsString(testJob);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/jobs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(testJobJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").isNumber()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.title").value(testJob.getTitle())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.company.id").value(testJob.getCompanyId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.company.name").value(testCompany.getName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.company.industry").value(testCompany.getIndustry().toString())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.company.website").value(testCompany.getWebsite())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.company.headquarters").value(testCompany.getHeadquarters())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.experienceLevel").value(testJob.getExperienceLevel().toString())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.type").value(testJob.getType().toString())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.workMode").value(testJob.getWorkMode().toString())
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
        CreateUpdateCompanyDto company = TestDataUtil.createTestCompanyDto();
        CompanyDto companyDto = companyService.save(company);

        CreateJobDto job = TestDataUtil.createTestJobDto(companyDto);
        jobService.save(job);
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/jobs")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.[0].id").value(1)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.[0].title").value(job.getTitle())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.[0].company.id").value(job.getCompanyId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.[0].experienceLevel").value(job.getExperienceLevel().toString())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.[0].type").value(job.getType().toString())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.[0].workMode").value(job.getWorkMode().toString())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.[0].description").value(job.getDescription())
        );
    }

    @Test
    public void testThatGetJobReturn200OkWhenJobExists() throws Exception {
        CreateUpdateCompanyDto company = TestDataUtil.createTestCompanyDto();
        CompanyDto companyDto = companyService.save(company);

        CreateJobDto job = TestDataUtil.createTestJobDto(companyDto);
        jobService.save(job);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/jobs/1")
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
        CreateUpdateCompanyDto company = TestDataUtil.createTestCompanyDto();
        CompanyDto companyDto = companyService.save(company);

        CreateJobDto job = TestDataUtil.createTestJobDto(companyDto);
        jobService.save(job);
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/jobs/1")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").value(1)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.title").value(job.getTitle())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.company.id").value(job.getCompanyId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.experienceLevel").value(job.getExperienceLevel().toString())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.type").value(job.getType().toString())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.workMode").value(job.getWorkMode().toString())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.description").value(job.getDescription())
        );
    }

    // update
    @Test
    public void testThatUpdateJobsReturnsHttp201Created() throws Exception {
        CreateUpdateCompanyDto testCompany1 = TestDataUtil.createTestCompanyDto();
        CompanyDto testCompany1Dto = companyService.save(testCompany1);

        CreateJobDto testJob1 = TestDataUtil.createTestJobDto(testCompany1Dto);
        jobService.save(testJob1);

        CreateUpdateCompanyDto testCompany2 = TestDataUtil.createTestCompany2Dto();
        CompanyDto testCompany2Dto = companyService.save(testCompany2);

        CreateJobDto testJob2 = TestDataUtil.createTestJobDto2(testCompany2Dto);
        String testJobJson2 = objectMapper.writeValueAsString(testJob2);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/api/jobs/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(testJobJson2)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatUpdateJobReturn404NotFoundWhenJobDoesNotExist() throws Exception {
        CreateUpdateCompanyDto testCompany2 = TestDataUtil.createTestCompany2Dto();
        CompanyDto testCompany2Dto = companyService.save(testCompany2);

        CreateJobDto testJob2 = TestDataUtil.createTestJobDto(testCompany2Dto);
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
        CreateUpdateCompanyDto testCompany1 = TestDataUtil.createTestCompanyDto();
        CompanyDto testCompany1Dto = companyService.save(testCompany1);
        CreateJobDto testJob1 = TestDataUtil.createTestJobDto(testCompany1Dto);
        JobResponseDto savedTestJob1 = jobService.save(testJob1);

        UpdateJobDto testJob2 = TestDataUtil.createTestUpdateJobDto2(testCompany1Dto);

        String testJobJson2 = objectMapper.writeValueAsString(testJob2);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/api/jobs/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(testJobJson2)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").value(savedTestJob1.getId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.title").value(testJob2.getTitle())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.company.id").value(testJob1.getCompanyId())
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
        CreateUpdateCompanyDto testCompany = TestDataUtil.createTestCompanyDto();
        CompanyDto testCompanyDto = companyService.save(testCompany);

        CreateJobDto testJob = TestDataUtil.createTestJobDto(testCompanyDto);
        JobResponseDto savedJob = jobService.save(testJob);

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/jobs/" + savedJob.getId())
        ).andExpect(
                MockMvcResultMatchers.status().isNoContent()
        );
    }
}
