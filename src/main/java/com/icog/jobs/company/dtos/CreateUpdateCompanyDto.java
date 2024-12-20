package com.icog.jobs.company.dtos;

import com.icog.jobs.company.models.Industry;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.UniqueElements;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateUpdateCompanyDto {

    @NotBlank(message = "Company Name must not be blank.")
    @Size(max = 100, message = "Company Name must be shorter than 100 characters.")
    private String name;

    @NotNull(message = "Industry must not be null.")
    private Industry industry;

    @Size(max = 255, message = "Website URL must not exceed 255 characters.")
    @Pattern(
            regexp = "^(http:\\/\\/|https:\\/\\/)?(www.)?([a-zA-Z0-9]+).[a-zA-Z0-9]*.[a-z]{3}.?([a-z]+)?$",
            message = "Website URL must be valid."
    )
    private String website;

    @NotNull(message = "Headquarters must not be null.")
    @Size(max = 255, message = "Headquarters must not exceed 255 characters.")
    private String headquarters;
}
