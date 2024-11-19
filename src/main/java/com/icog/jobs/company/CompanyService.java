package com.icog.jobs.company;

import com.icog.jobs.company.dtos.CompanyDto;
import com.icog.jobs.company.models.Company;
import com.icog.jobs.company.models.Industry;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CompanyService {

    CompanyRepository companyRepository;

    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public Company save(Company company) {
        return companyRepository.save(company);
    }

    List<Company> findAll() {
        return companyRepository.findAll();
    }

    Optional<Company> findOne(Integer id) {
        return companyRepository.findById(id);
    }

    Boolean isExisting(Integer id) {
        return companyRepository.existsById(id);
    }


    public List<Company> getCompanyByIndustry(Industry industry) {
        return companyRepository.findByIndustry(industry);
    }

    public Company partialUpdate(Integer id, Company company) {
        company.setId(id);

        return companyRepository.findById(id).map(existingCompany -> {
            Optional.ofNullable(company.getName()).ifPresent(existingCompany::setName);
            Optional.ofNullable(company.getIndustry()).ifPresent(existingCompany::setIndustry);
            Optional.ofNullable(company.getWebsite()).ifPresent(existingCompany::setWebsite);
            Optional.ofNullable(company.getHeadquarters()).ifPresent(existingCompany::setHeadquarters);
            return companyRepository.save(existingCompany);
        }).orElseThrow(() -> new RuntimeException("Could not find company with id: " + id));
    }

    public void delete(Integer id) {
        companyRepository.deleteById(id);
    }
}
