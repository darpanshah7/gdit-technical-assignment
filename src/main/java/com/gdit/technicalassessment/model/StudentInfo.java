package com.gdit.technicalassessment.model;

import lombok.Builder;
import java.time.LocalDate;

@Builder
public record StudentInfo(
        String firstName,
        String lastName,
        String ssn,
        LocalDate dateOfBirth
) {
}
