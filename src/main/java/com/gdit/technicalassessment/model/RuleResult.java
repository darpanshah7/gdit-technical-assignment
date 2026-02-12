package com.gdit.technicalassessment.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record RuleResult(
        String ruleName,
        String message
) {
}

