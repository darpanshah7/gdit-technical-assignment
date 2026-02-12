package com.gdit.technicalassessment.model;

import lombok.Builder;

@Builder
public record Application(
        StudentInfo studentInfo,
        DependencyStatus dependencyStatus,
        MaritalStatus maritalStatus,
        SpouseInfo spouseInfo,
        Household household,
        Income income,
        String stateOfResidence
) {
}
