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
        assertEquals(actual, expected);
    }

    @Test
    void ssnIsValidWithDashes() {
        var application = Application.builder()
                .studentInfo(StudentInfo.builder()
                        .ssn("123-45-6789")
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
        assertEquals(actual, expected);
    }

    @Test
    void ssnValidWithSpaces() {
        var application = Application.builder()
                .studentInfo(StudentInfo.builder()
                        .ssn("123 45 6 7 89")
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
        assertEquals(actual, expected);
    }

    @Test
    void ssnInvalidWhenTooShort() {
        var application = Application.builder()
                .studentInfo(StudentInfo.builder()
                        .ssn("12345678")
                        .build())
                .build();

        var expected = ValidationDetails.builder()
                .status(ValidationStatus.INVALID)
                .failedRules(List.of(
                        RuleResult.builder()
                                .ruleName("SSN_FORMAT")
                                .message("SSN must be exactly 9 digits. Provided: 12345678")
                                .build()
                ))
                .build();

        var actual = ssnRule.validate(application);
        assertEquals(actual, expected);
    }

    @Test
    void ssnInvalidWhenTooLong() {
        var application = Application.builder()
                .studentInfo(StudentInfo.builder()
                        .ssn("1234567890")
                        .build())
                .build();

        var expected = ValidationDetails.builder()
                .status(ValidationStatus.INVALID)
                .failedRules(List.of(
                        RuleResult.builder()
                                .ruleName("SSN_FORMAT")
                                .message("SSN must be exactly 9 digits. Provided: 1234567890")
                                .build()
                ))
                .build();

        var actual = ssnRule.validate(application);
        assertEquals(actual, expected);
    }

    @Test
    void ssnInvalidWithLetters() {
        var application = Application.builder()
                .studentInfo(StudentInfo.builder()
                        .ssn("12345678A")
                        .build())
                .build();

        var expected = ValidationDetails.builder()
                .status(ValidationStatus.INVALID)
                .failedRules(List.of(
                        RuleResult.builder()
                                .ruleName("SSN_FORMAT")
                                .message("SSN must be exactly 9 digits. Provided: 12345678A")
                                .build()
                ))
                .build();

        var actual = ssnRule.validate(application);
        assertEquals(actual, expected);
    }

    @Test
    void ssnInvalidWithInvalidFormat() {
        var application = Application.builder()
                .studentInfo(StudentInfo.builder()
                        .ssn("invalidFormat")
                        .build())
                .build();

        var expected = ValidationDetails.builder()
                .status(ValidationStatus.INVALID)
                .failedRules(List.of(
                        RuleResult.builder()
                                .ruleName("SSN_FORMAT")
                                .message("SSN must be exactly 9 digits. Provided: invalidFormat")
                                .build()
                ))
                .build();

        var actual = ssnRule.validate(application);
        assertEquals(actual, expected);
    }

    @Test
    void ssnInvalidWhenNull() {
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
        assertEquals(actual, expected);
    }

    @Test
    void ssnInvalidWhenEmpty() {
        var application = Application.builder()
                .studentInfo(StudentInfo.builder()
                        .ssn("")
                        .build())
                .build();

        var expected = ValidationDetails.builder()
                .status(ValidationStatus.INVALID)
                .failedRules(List.of(
                        RuleResult.builder()
                                .ruleName("SSN_FORMAT")
                                .message("SSN must be exactly 9 digits. Provided: ")
                                .build()
                ))
                .build();

        var actual = ssnRule.validate(application);
        assertEquals(actual, expected);
    }

    @Test
    void ssnInvalidWhenMissing() {
        var application = Application.builder()
                .studentInfo(StudentInfo.builder()
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
        assertEquals(actual, expected);
    }

    @Test
    void ssnInvalidWhenMissingStudentInfo() {
        var application = Application.builder()
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
        assertEquals(actual, expected);
    }
}

