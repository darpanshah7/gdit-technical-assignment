package com.gdit.technicalassessment.validation.rules;

import com.gdit.technicalassessment.model.*;
import com.gdit.technicalassessment.validation.ValidationRule;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class HouseholdRule implements ValidationRule {

    @Override
    public String getRuleName() {
        return "HOUSEHOLD_COLLEGE_COUNT";
    }

    @Override
    public boolean isActive() {
        return true;
    }

    @Override
    public ValidationDetails validate(Application application) {
        if (application.household() == null) {
            return buildInvalidResult("Household information is missing");
        }

        List<RuleResult> failedRules = new ArrayList<>();

        validateRequiredFields(application.household().numberInHousehold(),
                application.household().numberInCollege(), failedRules);

        if (!failedRules.isEmpty()) {
            return buildInvalidResult(failedRules);
        }

        validateNonNegativeValues(application.household().numberInHousehold(),
                application.household().numberInCollege(), failedRules);

        if (!failedRules.isEmpty()) {
            return buildInvalidResult(failedRules);
        }

        if (application.household().numberInCollege() > application.household().numberInHousehold()) {
            return buildInvalidResult(String.format(
                    "Number in college (%d) cannot exceed number in household (%d)",
                    application.household().numberInCollege(), application.household().numberInHousehold()));
        }

        return ValidationDetails.builder()
                .status(ValidationStatus.VALID)
                .passedRules(List.of(buildRuleResult(null)))
                .build();
    }

    private void validateRequiredFields(Integer numberInHousehold, Integer numberInCollege,
                                       List<RuleResult> failedRules) {
        if (numberInHousehold == null) {
            failedRules.add(buildRuleResult("Number in household is missing"));
        }
        if (numberInCollege == null) {
            failedRules.add(buildRuleResult("Number in college is missing"));
        }
    }

    private void validateNonNegativeValues(Integer numberInHousehold, Integer numberInCollege,
                                          List<RuleResult> failedRules) {
        if (numberInHousehold < 0) {
            failedRules.add(buildRuleResult(String.format(
                    "Number in household cannot be negative. Provided: %d", numberInHousehold)));
        }
        if (numberInCollege < 0) {
            failedRules.add(buildRuleResult(String.format(
                    "Number in college cannot be negative. Provided: %d", numberInCollege)));
        }
    }

    private RuleResult buildRuleResult(String message) {
        return RuleResult.builder()
                .ruleName(getRuleName())
                .message(message)
                .build();
    }

    private ValidationDetails buildInvalidResult(String message) {
        return buildInvalidResult(List.of(buildRuleResult(message)));
    }

    private ValidationDetails buildInvalidResult(List<RuleResult> failedRules) {
        return ValidationDetails.builder()
                .status(ValidationStatus.INVALID)
                .failedRules(failedRules)
                .build();
    }
}
