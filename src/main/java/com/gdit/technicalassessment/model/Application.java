package com.gdit.technicalassessment.model;

import lombok.Builder;

@Builder
public record Application(
        StudentInfo studentInfo,
        String dependencyStatus,
        String maritalStatus,
        SpouseInfo spouseInfo,
        Household household,
        Income income,
        String stateOfResidence
) {
}
