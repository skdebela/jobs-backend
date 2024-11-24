package com.icog.jobs.company;

import com.icog.jobs.company.dtos.CompanyDto;
import com.icog.jobs.company.dtos.CreateUpdateCompanyDto;
import com.icog.jobs.company.models.Company;
import com.icog.jobs.company.models.Industry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class CompanyService {

    private final CompanyMapper companyMapper;
    private final CompanyRepository companyRepository;

    @Autowired
    public CompanyService(CompanyRepository companyRepository, CompanyMapper companyMapper) {
        this.companyRepository = companyRepository;
        this.companyMapper = companyMapper;
    }

    public CompanyDto save(CreateUpdateCompanyDto companyDto) {
        Company company = companyMapper.mapFromCreateUpdate(companyDto);
        Company savedCompany = companyRepository.save(company);
        return companyMapper.mapTo(savedCompany);
    }

    public List<Company> findAll() {
        return companyRepository.findAll();
    }

    public Optional<Company> findOne(Integer id) {
        return companyRepository.findById(id);
    }

    public Boolean isExisting(CreateUpdateCompanyDto companyDto) {
        return companyRepository.existsByNameAndIndustry(companyDto.getName(), companyDto.getIndustry())
                ||
                companyRepository.existsByWebsite(companyDto.getWebsite())
                ;
    }


    public List<Company> getCompanyByIndustry(Industry industry) {
        return companyRepository.findByIndustry(industry);
    }

    public CompanyDto update(Integer id, CreateUpdateCompanyDto companyDto) {
        Company existingCompany = findOne(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        Company updatedCompany = companyMapper.mapFromCreateUpdate(companyDto);
        updatedCompany.setId(id);
        Company savedCompany = companyRepository.save(updatedCompany);

        return companyMapper.mapTo(savedCompany);
    }

    public CompanyDto createCompany(CreateUpdateCompanyDto companyDto) {
        if (isExisting(companyDto)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "A company with the same name and industry already exists.");
        }
        return save(companyDto);
    }

    public void delete(Integer id) {
        companyRepository.deleteById(id);
    }
}
