package com.icog.jobs.company.models;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Industry {
    TECHNOLOGY,
    FINANCE,
    HEALTHCARE,
    EDUCATION,
    CONSTRUCTION,
    RETAIL,
    MANUFACTURING,
    HOSPITALITY,
    AGRICULTURE;

    @JsonCreator
    public static Industry fromString(String industry) {
        return Industry.valueOf(industry.toUpperCase());
    }
}


