package com.gdit.technicalassessment.validation.rules;

import com.gdit.technicalassessment.model.Application;
import com.gdit.technicalassessment.model.RuleResult;
import com.gdit.technicalassessment.model.ValidationDetails;
import com.gdit.technicalassessment.model.ValidationStatus;
import com.gdit.technicalassessment.validation.ValidationRule;

import java.util.List;

public class SsnRule implements ValidationRule {

    @Override
    public String getRuleName() {
        return "SSN_FORMAT";
    }

    @Override
    public boolean isActive() {
        return true;
    }

    @Override
    public ValidationDetails validate(Application application) {
        if (application.studentInfo() == null || application.studentInfo().ssn() == null) {
            return ValidationDetails.builder()
                    .status(ValidationStatus.INVALID)
                    .failedRules(List.of(
                            RuleResult.builder()
                                    .ruleName(getRuleName())
                                    .message("Student SSN is missing")
                                    .build()
                    ))
                    .build();
        }

        String ssn = application.studentInfo().ssn();

        String cleanedSsn = ssn.replaceAll("[\\s-]", "");

        if (!cleanedSsn.matches("\\d{9}")) {
            return ValidationDetails.builder()
                    .status(ValidationStatus.INVALID)
                    .failedRules(List.of(
                            RuleResult.builder()
                                    .ruleName(getRuleName())
                                    .message(String.format("SSN must be exactly 9 digits. Provided: %s", ssn))
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
