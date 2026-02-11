package com.gdit.technicalassessment.model;

import lombok.Builder;

import java.util.List;

@Builder
public record ValidationDetails(
        ValidationStatus status,
        List<RuleResult> passedRules,
        List<RuleResult> failedRules
) {

}
