package com.gdit.technicalassessment.validation.rules;

import com.gdit.technicalassessment.model.Application;
import com.gdit.technicalassessment.model.MaritalStatus;
import com.gdit.technicalassessment.model.RuleResult;
import com.gdit.technicalassessment.model.SpouseInfo;
import com.gdit.technicalassessment.model.ValidationDetails;
import com.gdit.technicalassessment.model.ValidationStatus;
import com.gdit.technicalassessment.validation.ValidationRule;
import com.gdit.technicalassessment.validation.util.SsnValidator;

import java.util.ArrayList;
import java.util.List;

public class MarriedSpouseInfoRule implements ValidationRule {

    @Override
    public String getRuleName() {
        return "MARRIED_SPOUSE_INFO";
    }

    @Override
    public boolean isActive() {
        return true;
    }

    @Override
    public ValidationDetails validate(Application application) {
        if (application.maritalStatus() == null) {
            return buildInvalidResult("Marital status is missing");
        }

        if (application.maritalStatus() == MaritalStatus.SINGLE) {
            return validateSingleStudent(application);
        }

        if (application.spouseInfo() == null) {
            return buildInvalidResult("Married student must have spouse information");
        }

        return validateMarriedStudent(application.spouseInfo());
    }

    private ValidationDetails validateSingleStudent(Application application) {
        if (application.spouseInfo() != null) {
            return buildInvalidResult("Single student cannot have spouse information");
        }
        return buildValidResult();
    }

    private ValidationDetails validateMarriedStudent(SpouseInfo spouseInfo) {
        List<RuleResult> failedRules = new ArrayList<>();

        validateSpouse(spouseInfo, failedRules);

        if (!failedRules.isEmpty()) {
            return buildInvalidResult(failedRules);
        }

        return buildValidResult();
    }

    private void validateSpouse(SpouseInfo spouseInfo, List<RuleResult> failedRules) {
        if (isNullOrEmpty(spouseInfo.firstName())) {
            failedRules.add(buildRuleResult("Spouse first name is missing"));
        }

        if (isNullOrEmpty(spouseInfo.lastName())) {
            failedRules.add(buildRuleResult("Spouse last name is missing"));
        }

        if (isNullOrEmpty(spouseInfo.ssn())) {
            failedRules.add(buildRuleResult("Spouse SSN is missing"));
        } else if (!SsnValidator.isValidSsn(spouseInfo.ssn())) {
            failedRules.add(buildRuleResult(
                    String.format("Spouse SSN must be exactly 9 digits. Provided: %s", spouseInfo.ssn())));
        }
    }


    private boolean isNullOrEmpty(String value) {
        return value == null || value.isEmpty();
    }

    private RuleResult buildRuleResult(String message) {
        return RuleResult.builder()
                .ruleName(getRuleName())
                .message(message)
                .build();
    }

    private ValidationDetails buildValidResult() {
        return ValidationDetails.builder()
                .status(ValidationStatus.VALID)
                .passedRules(List.of(buildRuleResult(null)))
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

