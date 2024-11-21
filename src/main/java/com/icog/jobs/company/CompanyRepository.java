package com.icog.jobs.company;

import com.icog.jobs.company.models.Company;
import com.icog.jobs.company.models.Industry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Integer> {
    List<Company> findByIndustry(Industry industry);

    Boolean existsByNameAndIndustry(String name, Industry industry);
}
