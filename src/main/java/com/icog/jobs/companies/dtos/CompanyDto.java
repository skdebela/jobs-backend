package com.icog.jobs.companies.dtos;

import com.icog.jobs.companies.models.Industry;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompanyDto {
    private Integer id;
    private String name;
    private Industry industry;
    private String website;
    private String headquarters;
}
