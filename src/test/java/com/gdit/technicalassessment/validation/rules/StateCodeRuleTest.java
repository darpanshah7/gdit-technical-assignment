package com.gdit.technicalassessment.validation.rules;

import com.gdit.technicalassessment.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StateCodeRuleTest {

    private StateCodeRule stateCodeRule;

    @BeforeEach
    void setUp() {
        stateCodeRule = new StateCodeRule();
    }

    @Test
    void stateCodeIsValid() {
        var application = Application.builder()
                .stateOfResidence("MI")
                .build();

        var expected = ValidationDetails.builder()
                .status(ValidationStatus.VALID)
                .passedRules(List.of(
                        RuleResult.builder()
                                .ruleName("STATE_CODE")
                                .build()
                ))
                .build();

        var actual = stateCodeRule.validate(application);
        assertEquals(expected, actual);
    }


    @Test
    void stateCodeInvalidWhenMissing() {
        var application = Application.builder()
                .stateOfResidence(null)
                .build();

        var expected = ValidationDetails.builder()
                .status(ValidationStatus.INVALID)
                .failedRules(List.of(
                        RuleResult.builder()
                                .ruleName("STATE_CODE")
                                .message("State of residence is missing")
                                .build()
                ))
                .build();

        var actual = stateCodeRule.validate(application);
        assertEquals(expected, actual);
    }

    @Test
    void stateCodeInvalidWhenEmpty() {
        var application = Application.builder()
                .stateOfResidence("")
                .build();

        var expected = ValidationDetails.builder()
                .status(ValidationStatus.INVALID)
                .failedRules(List.of(
                        RuleResult.builder()
                                .ruleName("STATE_CODE")
                                .message("State of residence is missing")
                                .build()
                ))
                .build();

        var actual = stateCodeRule.validate(application);
        assertEquals(expected, actual);
    }

    @Test
    void stateCodeInvalidWhenInvalidCode() {
        var application = Application.builder()
                .stateOfResidence("XX")
                .build();

        var expected = ValidationDetails.builder()
                .status(ValidationStatus.INVALID)
                .failedRules(List.of(
                        RuleResult.builder()
                                .ruleName("STATE_CODE")
                                .message("Invalid state code. Provided: XX")
                                .build()
                ))
                .build();

        var actual = stateCodeRule.validate(application);
        assertEquals(expected, actual);
    }

    @Test
    void stateCodeInvalidWhenTooLong() {
        var application = Application.builder()
                .stateOfResidence("CAL")
                .build();

        var expected = ValidationDetails.builder()
                .status(ValidationStatus.INVALID)
                .failedRules(List.of(
                        RuleResult.builder()
                                .ruleName("STATE_CODE")
                                .message("Invalid state code. Provided: CAL")
                                .build()
                ))
                .build();

        var actual = stateCodeRule.validate(application);
        assertEquals(expected, actual);
    }

    @Test
    void stateCodeInvalidWhenTooShort() {
        var application = Application.builder()
                .stateOfResidence("C")
                .build();

        var expected = ValidationDetails.builder()
                .status(ValidationStatus.INVALID)
                .failedRules(List.of(
                        RuleResult.builder()
                                .ruleName("STATE_CODE")
                                .message("Invalid state code. Provided: C")
                                .build()
                ))
                .build();

        var actual = stateCodeRule.validate(application);
        assertEquals(expected, actual);
    }

    @Test
    void stateCodeInvalidWhenNumeric() {
        var application = Application.builder()
                .stateOfResidence("12")
                .build();

        var expected = ValidationDetails.builder()
                .status(ValidationStatus.INVALID)
                .failedRules(List.of(
                        RuleResult.builder()
                                .ruleName("STATE_CODE")
                                .message("Invalid state code. Provided: 12")
                                .build()
                ))
                .build();


        var actual = stateCodeRule.validate(application);
        assertEquals(expected, actual);
    }
}

