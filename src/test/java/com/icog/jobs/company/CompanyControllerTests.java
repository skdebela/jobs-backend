package com.icog.jobs.company;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.icog.jobs.TestDataUtil;
import com.icog.jobs.company.dtos.CompanyDto;
import com.icog.jobs.company.dtos.CreateUpdateCompanyDto;
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
        CreateUpdateCompanyDto testCompanyDto = TestDataUtil.createTestCompanyDto();
        String testCompanyJson = objectMapper.writeValueAsString(testCompanyDto);

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
        CreateUpdateCompanyDto testCompanyDto = TestDataUtil.createTestCompanyDto();
        String testCompanyJson = objectMapper.writeValueAsString(testCompanyDto);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/companies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(testCompanyJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").isNumber()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value(testCompanyDto.getName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.industry").value(testCompanyDto.getIndustry().toString())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.website").value(testCompanyDto.getWebsite())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.headquarters").value(testCompanyDto.getHeadquarters())
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
        CreateUpdateCompanyDto testCompanyDto = TestDataUtil.createTestCompanyDto();
        CompanyDto savedCompany = companyService.save(testCompanyDto);
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/companies/" + savedCompany.getId())
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatGetCompanySuccessfullyReturnCompanyIfCompanyExist() throws Exception {
        CreateUpdateCompanyDto testCompanyDto = TestDataUtil.createTestCompanyDto();
        CompanyDto savedTestCompany = companyService.save(testCompanyDto);
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/companies/" + savedTestCompany.getId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").isNumber()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value(testCompanyDto.getName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.industry").value(testCompanyDto.getIndustry().toString())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.website").value(testCompanyDto.getWebsite())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.headquarters").value(testCompanyDto.getHeadquarters())
        );
    }

    @Test
    public void testThatGetCompanyByIndustrySuccessfullyReturnCompanyWhenCompanyExist() throws Exception {
        CreateUpdateCompanyDto testCompany1Dto = TestDataUtil.createTestCompanyDto();
        companyService.save(testCompany1Dto);
        CreateUpdateCompanyDto testCompany4Dto = TestDataUtil.createTestCompany4Dto();
        companyService.save(testCompany4Dto);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/companies/industry/" + testCompany1Dto.getIndustry())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$", org.hamcrest.Matchers.hasSize(1))
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.[0].id").isNumber()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.[0].name").value(testCompany1Dto.getName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.[0].industry").value(testCompany1Dto.getIndustry().toString())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.[0].website").value(testCompany1Dto.getWebsite())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.[0].headquarters").value(testCompany1Dto.getHeadquarters())
        );
    }


    //TODO: test full update controller
    @Test
    public void testThatFullUpdateCompanySuccessfullyReturnHttp404WhenAuthorDoesNotExist() throws Exception {
        CreateUpdateCompanyDto testCompany2Dto = TestDataUtil.createTestCompany2Dto();
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
        CreateUpdateCompanyDto testCompanyDto = TestDataUtil.createTestCompanyDto();
        CompanyDto savedTestCompany = companyService.save(testCompanyDto);

        CreateUpdateCompanyDto testCompany2Dto = TestDataUtil.createTestCompany2Dto();
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
        CreateUpdateCompanyDto testCompanyDto = TestDataUtil.createTestCompanyDto();
        CompanyDto savedTestCompany = companyService.save(testCompanyDto);

        CreateUpdateCompanyDto testCompany2Dto = TestDataUtil.createTestCompany2Dto();
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
        CreateUpdateCompanyDto testCompanyDto = TestDataUtil.createTestCompanyDto();
        CompanyDto savedTestCompany = companyService.save(testCompanyDto);

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/companies/" + savedTestCompany.getId())
        ).andExpect(
                MockMvcResultMatchers.status().isNoContent()
        );
    }

}
