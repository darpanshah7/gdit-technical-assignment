package com.gdit.technicalassessment.validation.rules;

import com.gdit.technicalassessment.model.*;
import com.gdit.technicalassessment.validation.ValidationRule;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.gdit.technicalassessment.model.DependencyStatus.INDEPENDENT;
import static com.gdit.technicalassessment.validation.util.EnumValidator.isValidEnum;
import static com.gdit.technicalassessment.validation.util.EnumValidator.parseEnum;

@Component
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
            return buildInvalidResult("Dependency status is missing");
        }

        if (!isValidEnum(application.dependencyStatus(), DependencyStatus.class)) {
            return buildInvalidResult(String.format("Invalid dependency status. Must be one of the following: %s, Provided: %s", List.of(DependencyStatus.values()), application.dependencyStatus()));
        }

        if (parseEnum(application.dependencyStatus(), DependencyStatus.class) == INDEPENDENT) {
            return buildValidResultForIndependent();
        }

        if (application.income() == null) {
            return buildInvalidResult("Dependent student must have income information with parent income");
        }

        if (application.income().parentIncome() == null) {
            return buildInvalidResult("Dependent student must have parent income");
        }

        return ValidationDetails.builder()
                .status(ValidationStatus.VALID)
                .passedRules(List.of(buildRuleResult(null)))
                .build();
    }

    private RuleResult buildRuleResult(String message) {
        return RuleResult.builder()
                .ruleName(getRuleName())
                .message(message)
                .build();
    }

    private ValidationDetails buildValidResultForIndependent() {
        return ValidationDetails.builder()
                .status(ValidationStatus.VALID)
                .passedRules(List.of(buildRuleResult(null)))
                .build();
    }

    private ValidationDetails buildInvalidResult(String message) {
        return ValidationDetails.builder()
                .status(ValidationStatus.INVALID)
                .failedRules(List.of(buildRuleResult(message)))
                .build();
    }
}
