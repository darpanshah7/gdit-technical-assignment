package com.gdit.technicalassessment.service;

import com.gdit.technicalassessment.model.Application;
import com.gdit.technicalassessment.model.RuleResult;
import com.gdit.technicalassessment.model.ValidationDetails;
import com.gdit.technicalassessment.model.ValidationStatus;
import com.gdit.technicalassessment.validation.ValidationRule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Slf4j
@Service
public class ApplicationValidationService {

    private final Collection<ValidationRule> rules;

    public ApplicationValidationService(Collection<ValidationRule> rules) {
        this.rules = rules;
        log.info("ApplicationValidationService initialized with {} validation rules", rules.size());
    }

    public ValidationDetails validateApplication(Application application) {
        log.info("Starting validation for application");

        List<RuleResult> allPassedRules = new ArrayList<>();
        List<RuleResult> allFailedRules = new ArrayList<>();

        for (ValidationRule rule : rules) {
            if (rule.isActive()) {
                log.debug("Applying validation rule: {}", rule.getRuleName());
                ValidationDetails result = rule.validate(application);

                if (result.passedRules() != null) {
                    allPassedRules.addAll(result.passedRules());
                }

                if (result.failedRules() != null) {
                    allFailedRules.addAll(result.failedRules());
                    log.info("Rule {} failed with {} error(s)", rule.getRuleName(), result.failedRules().size());
                }
            }
        }

        ValidationStatus overallStatus = allFailedRules.isEmpty()
                ? ValidationStatus.VALID
                : ValidationStatus.INVALID;

        log.info("Validation complete. Status: {}, Passed: {}, Failed: {}",
                overallStatus, allPassedRules.size(), allFailedRules.size());

        return ValidationDetails.builder()
                .status(overallStatus)
                .passedRules(allPassedRules)
                .failedRules(allFailedRules)
                .build();
    }
}
