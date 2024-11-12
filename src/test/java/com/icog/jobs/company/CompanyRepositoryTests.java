package com.icog.jobs.company;

import com.icog.jobs.TestDataUtil;
import com.icog.jobs.company.models.Company;
import com.icog.jobs.company.models.Industry;
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
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class CompanyRepositoryTests {
    private CompanyRepository underTest;

    @Autowired
    public CompanyRepositoryTests(CompanyRepository companyRepository) {
        this.underTest = companyRepository;
    }

    @Test
    public void testThatCompanyCanBeCreatedAndRecalled() {
        Company company = TestDataUtil.createTestCompany();
        underTest.save(company);
        Optional<Company> result = underTest.findById(company.getId());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(company);
    }

    @Test
    public void testThatMultipleCompaniesCanBeCreatedAndRecalled() {
        Company company1 = TestDataUtil.createTestCompany();
        underTest.save(company1);
        Company company2 = TestDataUtil.createTestCompany2();
        underTest.save(company2);
        Company company3 = TestDataUtil.createTestCompany3();
        underTest.save(company3);

        Iterable<Company> result = underTest.findAll();
        assertThat(result)
                .hasSize(3)
                .containsExactlyInAnyOrder(company1, company2, company3);
    }

    @Test
    public void testThatCompanyCanBeUpdatedAndRecalled() {
        Company company = TestDataUtil.createTestCompany();
        underTest.save(company);
        company.setName("UPDATED");
        underTest.save(company);
        Optional<Company> result = underTest.findById(company.getId());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(company);
    }

    @Test
    public void testThatCompanyCanBeDeleted() {
        Company company = TestDataUtil.createTestCompany();
        underTest.save(company);
        underTest.deleteById(company.getId());
        Optional<Company> result = underTest.findById(company.getId());
        assertThat(result).isEmpty();
    }

    @Test
    public void testThatGetCompaniesByIndustryGenerateCorrectSql() {
        Company company1 = TestDataUtil.createTestCompany();
        underTest.save(company1);
        Company company2 = TestDataUtil.createTestCompany2();
        underTest.save(company2);
        Company company3 = TestDataUtil.createTestCompany3();
        underTest.save(company3);
        Company company4 = TestDataUtil.createTestCompany4();
        underTest.save(company4);

        Iterable<Company> result = underTest.findByIndustry(Industry.RETAIL);
        assertThat(result).containsExactly(company4);
    }
}
