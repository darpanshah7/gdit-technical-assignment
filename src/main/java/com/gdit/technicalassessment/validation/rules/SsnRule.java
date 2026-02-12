package com.gdit.technicalassessment.validation.rules;

import com.gdit.technicalassessment.model.Application;
import com.gdit.technicalassessment.model.RuleResult;
import com.gdit.technicalassessment.model.ValidationDetails;
import com.gdit.technicalassessment.model.ValidationStatus;
import com.gdit.technicalassessment.validation.ValidationRule;

import java.util.List;

public class SsnRule implements ValidationRule {

    private static final String SSN_PATTERN = "\\d{9}";

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
        if (!hasSsnInformation(application)) {
            return buildInvalidResult("Student SSN is missing");
        }

        String ssn = application.studentInfo().ssn();
        String cleanedSsn = cleanSsn(ssn);

        if (!isValidSsnFormat(cleanedSsn)) {
            return buildInvalidResult(String.format("SSN must be exactly 9 digits. Provided: %s", ssn));
        }

        return ValidationDetails.builder()
                .status(ValidationStatus.VALID)
                .passedRules(List.of(buildRuleResult(null)))
                .build();    }

    private boolean hasSsnInformation(Application application) {
        return application.studentInfo() != null && application.studentInfo().ssn() != null;
    }

    private String cleanSsn(String ssn) {
        return ssn.replaceAll("[\\s-]", "");
    }

    private boolean isValidSsnFormat(String cleanedSsn) {
        return cleanedSsn.matches(SSN_PATTERN);
    }

    private RuleResult buildRuleResult(String message) {
        return RuleResult.builder()
                .ruleName(getRuleName())
                .message(message)
                .build();
    }

    private ValidationDetails buildInvalidResult(String message) {
        return ValidationDetails.builder()
                .status(ValidationStatus.INVALID)
                .failedRules(List.of(buildRuleResult(message)))
                .build();
    }
}
