package com.gdit.technicalassessment.validation.rules;

import com.gdit.technicalassessment.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SsnRuleTest {

    private SsnRule ssnRule;

    @BeforeEach
    void setUp() {
        ssnRule = new SsnRule();
    }

    @Test
    void ssnIsValidFormat() {
        var application = Application.builder()
                .studentInfo(StudentInfo.builder()
                        .ssn("123456789")
                        .build())
                .build();

        var expected = ValidationDetails.builder()
                .status(ValidationStatus.VALID)
                .passedRules(List.of(
                        RuleResult.builder()
                                .ruleName("SSN_FORMAT")
                                .build()
                ))
                .build();

        var actual = ssnRule.validate(application);
        assertEquals(expected, actual);
    }

    @Test
    void ssnIsInvalidFormat() {
            var application = Application.builder()
                    .studentInfo(StudentInfo.builder()
                            .ssn("invalid")
                            .build())
                    .build();

            var expected = ValidationDetails.builder()
                    .status(ValidationStatus.INVALID)
                    .failedRules(List.of(
                            RuleResult.builder()
                                    .ruleName("SSN_FORMAT")
                                    .message("SSN must be exactly 9 digits. Provided: invalid")
                                    .build()
                    ))
                    .build();

            var actual = ssnRule.validate(application);
            assertEquals(expected, actual);
        }


    @Test
    void ssnIsMissing() {
        var application = Application.builder()
                .studentInfo(StudentInfo.builder()
                        .ssn(null)
                        .build())
                .build();

        var expected = ValidationDetails.builder()
                .status(ValidationStatus.INVALID)
                .failedRules(List.of(
                        RuleResult.builder()
                                .ruleName("SSN_FORMAT")
                                .message("Student SSN is missing")
                                .build()
                ))
                .build();

        var actual = ssnRule.validate(application);
        assertEquals(expected, actual);
    }

    @Test
    void studentInfoIsMissing() {
        var application = Application.builder()
                .studentInfo(null)
                .build();

        var expected = ValidationDetails.builder()
                .status(ValidationStatus.INVALID)
                .failedRules(List.of(
                        RuleResult.builder()
                                .ruleName("SSN_FORMAT")
                                .message("Student SSN is missing")
                                .build()
                ))
                .build();

        var actual = ssnRule.validate(application);
        assertEquals(expected, actual);
    }
}

