package com.gdit.technicalassessment.model;

public record Application(
        StudentInfo studentInfo,
        DependencyStatus dependencyStatus,
        MaritalStatus maritalStatus,
        SpouseInfo spouseInfo,
        Household household,
        Income income,
        UsaState stateOfResidence
) {
}
