package com.gdit.technicalassessment.model;

import lombok.Builder;

@Builder
public record RuleResult(
        String ruleName,
        String message
) {
}

