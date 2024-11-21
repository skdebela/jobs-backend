package com.icog.jobs.company;

import com.icog.jobs.Mapper;
import com.icog.jobs.company.dtos.CompanyDto;
import com.icog.jobs.company.models.Company;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class CompanyMapper implements Mapper<Company, CompanyDto> {
    private ModelMapper modelMapper;

    public CompanyMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public CompanyDto mapTo(Company company) {
        return modelMapper.map(company, CompanyDto.class);
    }

    @Override
    public Company mapFrom(CompanyDto companyDto) {
        return modelMapper.map(companyDto, Company.class);
    }
}
