package com.icog.jobs.company;

import com.icog.jobs.Mapper;
import com.icog.jobs.company.dtos.CompanyDto;
import com.icog.jobs.company.models.Company;
import com.icog.jobs.company.models.Industry;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class CompanyController {
    private final CompanyService companyService;
    private final Mapper<Company, CompanyDto> companyMapper;


    public CompanyController(CompanyService companyService, Mapper<Company, CompanyDto> companyMapper) {
        this.companyService = companyService;
        this.companyMapper = companyMapper;
    }


    @Operation(summary = "Create a company"
            , description = "Add a new company to the database.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Company created successfully."),
            @ApiResponse(responseCode = "404", description = "Invalid input.")
    })
    @PostMapping(path = "api/companies")
    ResponseEntity<CompanyDto> createCompany(@RequestBody CompanyDto companyDto) {
        Company company = companyMapper.mapFrom(companyDto);
        Company savedCompany = companyService.save(company);
        CompanyDto savedCompanyDto = companyMapper.mapTo(savedCompany);
        return new ResponseEntity<>(savedCompanyDto, HttpStatus.CREATED);
    }


    @Operation(summary = "Get all companies."
            , description = "Fetch all companies.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved companies."),
            @ApiResponse(responseCode = "404", description = "No Company found.")
    })
    @GetMapping(path = "api/companies")
    public ResponseEntity<List<CompanyDto>> listCompanies() {
        List<Company> companies = companyService.findAll();
        List<CompanyDto> companiesDto = companies.stream().map(companyMapper::mapTo).collect(Collectors.toList());
        return new ResponseEntity<>(companiesDto, HttpStatus.OK);
    }


    @Operation(summary = "Get a company."
            , description = "Fetch a company by id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved company."),
            @ApiResponse(responseCode = "404", description = "No Company found.")
    })
    @GetMapping(path = "api/companies/{id}")
    public ResponseEntity<CompanyDto> getCompany(@PathVariable Integer id) {
        Optional<Company> foundCompany = companyService.findOne(id);
        return foundCompany.map(company -> {
            CompanyDto companyDto = companyMapper.mapTo(company);
            return new ResponseEntity<>(companyDto, HttpStatus.OK);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }


    @Operation(summary = "Get a company by industry."
            , description = "Fetch all companies belonging to a specific industry.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved companies."),
            @ApiResponse(responseCode = "404", description = "No Company found for the given industry")
    })
    @GetMapping(path = "api/companies/industry/{industry}")
    public ResponseEntity<List<CompanyDto>> getCompanyByIndustry(@PathVariable Industry industry) {
        List<Company> companies = companyService.getCompanyByIndustry(industry);
        List<Company> foundCompanies = companyService.getCompanyByIndustry(industry);

        List<CompanyDto> companiesDto = companies.stream().map(companyMapper::mapTo).collect(Collectors.toList());
        return new ResponseEntity<>(companiesDto, HttpStatus.OK);
    }


    @Operation(summary = "Fully update a company."
            , description = "")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Company successfully updated."),
            @ApiResponse(responseCode = "404", description = "Company not found.")
    })
    @PutMapping(path = "/api/companies/{id}")
    public ResponseEntity<CompanyDto> fullUpdateCompany(
            @PathVariable("id") Integer id,
            @RequestBody CompanyDto companyDto
    ) {
        if (!companyService.isExisting(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        companyDto.setId(id);
        Company company = companyMapper.mapFrom(companyDto);
        Company savedCompany = companyService.save(company);
        CompanyDto savedCompanyDto = companyMapper.mapTo(savedCompany);
        return new ResponseEntity<>(savedCompanyDto, HttpStatus.OK);
    }


    @Operation(summary = "Partially update a company."
            , description = "")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Company successfully updated."),
            @ApiResponse(responseCode = "404", description = "Company not found.")
    })
    @PatchMapping(path = "/api/companies/{id}")
    public ResponseEntity<CompanyDto> partiallyUpdateCompany(
            @PathVariable("id") Integer id,
            @RequestBody CompanyDto companyDto
    ) {
        if (!companyService.isExisting(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        companyDto.setId(id);
        Company company = companyMapper.mapFrom(companyDto);
        Company savedCompany = companyService.partialUpdate(id, company);
        CompanyDto savedCompanyDto = companyMapper.mapTo(savedCompany);
        return new ResponseEntity<>(savedCompanyDto, HttpStatus.OK);
    }


    @Operation(summary = "Delete a company."
            , description = "")
    @ApiResponse(responseCode = "204", description = "Company deleted.")
    @DeleteMapping(path = "/api/companies/{id}")
    public ResponseEntity deleteCompany(@PathVariable("id") Integer id) {
        companyService.delete(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
