package com.icog.jobs;

import com.icog.jobs.company.dtos.CompanyDto;
import com.icog.jobs.company.models.Company;
import com.icog.jobs.company.models.Industry;

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

}
