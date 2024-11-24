package com.icog.jobs.company;

import com.icog.jobs.company.dtos.CompanyDto;
import com.icog.jobs.company.dtos.CreateUpdateCompanyDto;
import com.icog.jobs.company.models.Company;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CompanyMapper {
    private ModelMapper modelMapper;

    @Autowired
    public CompanyMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public CompanyDto mapTo(Company company) {
        return modelMapper.map(company, CompanyDto.class);
    }

    public Company mapFromCreateUpdate(CreateUpdateCompanyDto companyDto) {
        return modelMapper.map(companyDto, Company.class);
    }
}
