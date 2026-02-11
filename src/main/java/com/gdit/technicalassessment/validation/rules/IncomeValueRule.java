package com.gdit.technicalassessment.validation.rules;

import com.gdit.technicalassessment.model.Application;
import com.gdit.technicalassessment.model.RuleResult;
import com.gdit.technicalassessment.model.ValidationDetails;
import com.gdit.technicalassessment.model.ValidationStatus;
import com.gdit.technicalassessment.validation.ValidationRule;

import java.util.ArrayList;
import java.util.List;

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
            return ValidationDetails.builder()
                    .status(ValidationStatus.INVALID)
                    .failedRules(List.of(
                            RuleResult.builder()
                                    .ruleName(getRuleName())
                                    .message("Income information is missing")
                                    .build()
                    ))
                    .build();
        }

        List<RuleResult> failedRules = new ArrayList<>();

        Integer studentIncome = application.income().studentIncome();
        Integer parentIncome = application.income().parentIncome();

        if (studentIncome != null && studentIncome < 0) {
            failedRules.add(RuleResult.builder()
                    .ruleName(getRuleName())
                    .message(String.format("Student income cannot be negative. Provided: %d", studentIncome))
                    .build());
        }

        if (parentIncome != null && parentIncome < 0) {
            failedRules.add(RuleResult.builder()
                    .ruleName(getRuleName())
                    .message(String.format("Parent income cannot be negative. Provided: %d", parentIncome))
                    .build());
        }

        if (!failedRules.isEmpty()) {
            return ValidationDetails.builder()
                    .status(ValidationStatus.INVALID)
                    .failedRules(failedRules)
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
