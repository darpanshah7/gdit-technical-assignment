package com.gdit.technicalassessment.validation.rules;

import com.gdit.technicalassessment.model.Application;
import com.gdit.technicalassessment.model.RuleResult;
import com.gdit.technicalassessment.model.ValidationDetails;
import com.gdit.technicalassessment.model.ValidationStatus;
import com.gdit.technicalassessment.validation.ValidationRule;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class IncomeValueRule implements ValidationRule {

    @Override
    public String getRuleName() {
        return "INCOME_VALUE";
    }

    @Override
    public boolean isActive() {
        return true;
    }

    @Override
    public ValidationDetails validate(Application application) {
        if (application.income() == null) {
            return buildInvalidResult("Income information is missing");
        }

        List<RuleResult> failedRules = new ArrayList<>();

        validateStudentIncome(application.income().studentIncome(), failedRules);
        validateParentIncome(application.income().parentIncome(), failedRules);

        if (!failedRules.isEmpty()) {
            return buildInvalidResult(failedRules);
        }

        return ValidationDetails.builder()
                .status(ValidationStatus.VALID)
                .passedRules(List.of(buildRuleResult(null)))
                .build();
    }

    private void validateStudentIncome(Integer studentIncome, List<RuleResult> failedRules) {
        if (studentIncome != null && studentIncome < 0) {
            failedRules.add(buildRuleResult(
                    String.format("Student income cannot be negative. Provided: %d", studentIncome)));
        }
    }

    private void validateParentIncome(Integer parentIncome, List<RuleResult> failedRules) {
        if (parentIncome != null && parentIncome < 0) {
            failedRules.add(buildRuleResult(
                    String.format("Parent income cannot be negative. Provided: %d", parentIncome)));
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
