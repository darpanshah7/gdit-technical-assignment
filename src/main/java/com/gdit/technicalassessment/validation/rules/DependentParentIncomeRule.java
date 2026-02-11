package com.gdit.technicalassessment.validation.rules;

import com.gdit.technicalassessment.model.Application;
import com.gdit.technicalassessment.model.DependencyStatus;
import com.gdit.technicalassessment.model.RuleResult;
import com.gdit.technicalassessment.model.ValidationDetails;
import com.gdit.technicalassessment.model.ValidationStatus;
import com.gdit.technicalassessment.validation.ValidationRule;

import java.util.List;

public class DependentParentIncomeRule implements ValidationRule {

    @Override
    public String getRuleName() {
        return "DEPENDENT_PARENT_INCOME";
    }

    @Override
    public boolean isActive() {
        return true;
    }

    @Override
    public ValidationDetails validate(Application application) {
        if (application.dependencyStatus() == null) {
            return ValidationDetails.builder()
                    .status(ValidationStatus.INVALID)
                    .failedRules(List.of(
                            RuleResult.builder()
                                    .ruleName(getRuleName())
                                    .message("Dependency status is missing")
                                    .build()
                    ))
                    .build();
        }

        if (application.dependencyStatus() != DependencyStatus.DEPENDENT) {
            return ValidationDetails.builder()
                    .status(ValidationStatus.VALID)
                    .passedRules(List.of(
                            RuleResult.builder()
                                    .ruleName(getRuleName())
                                    .build()
                    ))
                    .build();
        }

        if (application.income() == null) {
            return ValidationDetails.builder()
                    .status(ValidationStatus.INVALID)
                    .failedRules(List.of(
                            RuleResult.builder()
                                    .ruleName(getRuleName())
                                    .message("Dependent student must have income information with parent income")
                                    .build()
                    ))
                    .build();
        }

        if (application.income().parentIncome() == null) {
            return ValidationDetails.builder()
                    .status(ValidationStatus.INVALID)
                    .failedRules(List.of(
                            RuleResult.builder()
                                    .ruleName(getRuleName())
                                    .message("Dependent student must have parent income")
                                    .build()
                    ))
                    .build();
        }

        return ValidationDetails.builder()
                .status(ValidationStatus.VALID)
                .passedRules(List.of(
                        RuleResult.builder()
                                .ruleName(getRuleName())
                                .build()
                ))
                .build();
    }
}
