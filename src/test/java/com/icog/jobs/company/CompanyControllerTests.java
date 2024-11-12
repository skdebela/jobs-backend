package com.icog.jobs.company;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.icog.jobs.TestDataUtil;
import com.icog.jobs.company.dtos.CompanyDto;
import com.icog.jobs.company.models.Company;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
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
public class CompanyControllerTests {
    private final CompanyService companyService;
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    @Autowired
    public CompanyControllerTests(CompanyService companyService, MockMvc mockMvc, ObjectMapper objectMapper) {
        this.companyService = companyService;
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }

    @Test
    public void testThatCreateCompanySuccessfullyReturnHttp201Created() throws Exception {
        Company testCompany = TestDataUtil.createTestCompany();
        String testCompanyJson = objectMapper.writeValueAsString(testCompany);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/companies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(testCompanyJson)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        );
    }


    @Test
    public void testThatCreateCompanySuccessfullyReturnCreatedCompany() throws Exception {
        Company testCompany = TestDataUtil.createTestCompany();
        String testCompanyJson = objectMapper.writeValueAsString(testCompany);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/companies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(testCompanyJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").isNumber()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value(testCompany.getName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.industry").value(testCompany.getIndustry().toString())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.website").value(testCompany.getWebsite())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.headquarters").value(testCompany.getHeadquarters())
        );
    }

    @Test
    public void testThatGetCompanySuccessfullyReturnHttp404NotFoundIfCompanyDoesNotExist() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/companies/" + 1)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    public void testThatGetCompanySuccessfullyReturnHttp200OkIfCompanyExist() throws Exception {
        Company testCompany = TestDataUtil.createTestCompany();
        companyService.save(testCompany);
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/companies/" + testCompany.getId())
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatGetCompanySuccessfullyReturnCompanyIfCompanyExist() throws Exception {
        Company testCompany = TestDataUtil.createTestCompany();
        Company savedTestCompany = companyService.save(testCompany);
        String testCompanyJson = objectMapper.writeValueAsString(savedTestCompany);
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/companies/" + testCompany.getId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").isNumber()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value(testCompany.getName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.industry").value(testCompany.getIndustry().toString())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.website").value(testCompany.getWebsite())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.headquarters").value(testCompany.getHeadquarters())
        );
    }

    @Test
    public void testThatGetCompanyByIndustrySuccessfullyReturnCompanyWhenCompanyExist() throws Exception {
        Company testCompany1 = TestDataUtil.createTestCompany();
        companyService.save(testCompany1);
        Company testCompany4 = TestDataUtil.createTestCompany4();
        companyService.save(testCompany4);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/companies/industry/" + testCompany1.getIndustry())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$", org.hamcrest.Matchers.hasSize(1))
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.[0].id").isNumber()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.[0].name").value(testCompany1.getName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.[0].industry").value(testCompany1.getIndustry().toString())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.[0].website").value(testCompany1.getWebsite())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.[0].headquarters").value(testCompany1.getHeadquarters())
        );
    }


    //TODO: test full update controller
    @Test
    public void testThatFullUpdateCompanySuccessfullyReturnHttp404WhenAuthorDoesNotExist() throws Exception {
        CompanyDto testCompany2Dto = TestDataUtil.createTestCompany2Dto();
        String testCompany2DtoJson = objectMapper.writeValueAsString(testCompany2Dto);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/api/companies/" + 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(testCompany2DtoJson)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }
    @Test
    public void testThatFullUpdateCompanySuccessfullyReturnHttp200WhenAuthorExist() throws Exception {
        Company testCompany = TestDataUtil.createTestCompany();
        Company savedTestCompany = companyService.save(testCompany);

        CompanyDto testCompany2Dto = TestDataUtil.createTestCompany2Dto();
        String testCompany2DtoJson = objectMapper.writeValueAsString(testCompany2Dto);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/api/companies/" + savedTestCompany.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(testCompany2DtoJson)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatFullUpdateCompanySuccessfullyReturnUpdatedCompany() throws Exception {
        Company testCompany = TestDataUtil.createTestCompany();
        Company savedTestCompany = companyService.save(testCompany);

        CompanyDto testCompany2Dto = TestDataUtil.createTestCompany2Dto();
        String testCompany2DtoJson = objectMapper.writeValueAsString(testCompany2Dto);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/api/companies/" + savedTestCompany.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(testCompany2DtoJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").value(savedTestCompany.getId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value(testCompany2Dto.getName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.industry").value(testCompany2Dto.getIndustry().toString())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.website").value(testCompany2Dto.getWebsite())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.headquarters").value(testCompany2Dto.getHeadquarters())
        );
    }

    //TODO: test partial update controller
    @Test
    public void testThatPartialUpdateCompanySuccessfullyReturnHttp404WhenAuthorDoesNotExist() throws Exception {
        CompanyDto testCompany2Dto = TestDataUtil.createTestCompany2Dto();
        String testCompany2DtoJson = objectMapper.writeValueAsString(testCompany2Dto);

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/api/companies/" + 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(testCompany2DtoJson)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }
    @Test
    public void testThatPartialUpdateCompanySuccessfullyReturnHttp200WhenAuthorExist() throws Exception {
        Company testCompany = TestDataUtil.createTestCompany();
        Company savedTestCompany = companyService.save(testCompany);

        CompanyDto testCompanyDto = CompanyDto.builder()
                .headquarters("Debre Birhan, Ethiopia")
                .build();
        String testCompanyDtoJson = objectMapper.writeValueAsString(testCompanyDto);

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/api/companies/" + savedTestCompany.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(testCompanyDtoJson)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatPartialUpdateCompanySuccessfullyReturnUpdatedCompany() throws Exception {
        Company testCompany = TestDataUtil.createTestCompany();
        Company savedTestCompany = companyService.save(testCompany);

        CompanyDto testCompanyDto = CompanyDto.builder()
                .headquarters("Debre Birhan, Ethiopia")
                .build();
        String testCompanyDtoJson = objectMapper.writeValueAsString(testCompanyDto);

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/api/companies/" + savedTestCompany.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(testCompanyDtoJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").value(savedTestCompany.getId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value(testCompany.getName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.industry").value(testCompany.getIndustry().toString())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.website").value(testCompany.getWebsite())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.headquarters").value(testCompanyDto.getHeadquarters())
        );
    }
    //TODO: test delete controller
    @Test
    public void testThatDeleteCompanyReturnsHttpStatus204ForNonExistingCompanies() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/companies/" + 1)
        ).andExpect(
                MockMvcResultMatchers.status().isNoContent()
        );
    }

    @Test
    public void testThatDeleteCompanyReturnsHttpStatus204ForExistingCompanies() throws Exception {
        Company testCompany = TestDataUtil.createTestCompany();
        Company savedTestCompany = companyService.save(testCompany);

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/companies/" + savedTestCompany.getId())
        ).andExpect(
                MockMvcResultMatchers.status().isNoContent()
        );
    }

}
