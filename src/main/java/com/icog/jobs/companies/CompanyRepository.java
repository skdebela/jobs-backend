package com.icog.jobs.companies;

import com.icog.jobs.companies.models.Company;
import com.icog.jobs.companies.models.Industry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Integer> {
    List<Company> findByIndustry(Industry industry);

    Boolean existsByWebsite(String website);

    Boolean existsByNameAndIndustry(String name, Industry industry);
}
