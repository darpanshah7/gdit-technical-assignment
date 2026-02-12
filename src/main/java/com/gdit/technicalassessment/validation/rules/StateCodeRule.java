package com.gdit.technicalassessment.validation.rules;

import com.gdit.technicalassessment.model.Application;
import com.gdit.technicalassessment.model.RuleResult;
import com.gdit.technicalassessment.model.UsaState;
import com.gdit.technicalassessment.model.ValidationDetails;
import com.gdit.technicalassessment.model.ValidationStatus;
import com.gdit.technicalassessment.validation.ValidationRule;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.gdit.technicalassessment.validation.util.EnumValidator.isValidEnum;

@Component
public class StateCodeRule implements ValidationRule {

    @Override
    public String getRuleName() {
        return "STATE_CODE";
    }

    @Override
    public boolean isActive() {
        return true;
    }

    @Override
    public ValidationDetails validate(Application application) {
        String stateCode = application.stateOfResidence();

        if (stateCode == null || stateCode.isEmpty()) {
            return buildInvalidResult("State of residence is missing");
        }

        if (!isValidEnum(stateCode, UsaState.class)) {
            return buildInvalidResult(String.format("Invalid state code. Provided: %s", stateCode));
        }

        return ValidationDetails.builder()
            .status(ValidationStatus.VALID)
            .passedRules(List.of(buildRuleResult(null)))
            .build();
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

