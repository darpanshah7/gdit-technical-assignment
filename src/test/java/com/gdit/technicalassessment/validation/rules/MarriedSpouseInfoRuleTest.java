package com.gdit.technicalassessment.validation.rules;

import com.gdit.technicalassessment.model.*;
import com.gdit.technicalassessment.validation.util.SsnValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class MarriedSpouseInfoRuleTest {

    private MarriedSpouseInfoRule marriedSpouseInfoRule;

    @BeforeEach
    void setUp() {
        marriedSpouseInfoRule = new MarriedSpouseInfoRule();
    }

    @Test
    void marriedStudentWithSpouseInfoIsValid() {
        var application = Application.builder()
                .maritalStatus(MaritalStatus.MARRIED)
                .spouseInfo(new SpouseInfo("John", "Doe", "987654321"))
                .build();

        var expected = ValidationDetails.builder()
                .status(ValidationStatus.VALID)
                .passedRules(List.of(
                        RuleResult.builder()
                                .ruleName("MARRIED_SPOUSE_INFO")
                                .build()
                ))
                .build();

        var actual = marriedSpouseInfoRule.validate(application);
        assertEquals(expected, actual);
    }

    @Test
    void marriedStudentWithoutSpouseInfoIsInvalid() {
        var application = Application.builder()
                .maritalStatus(MaritalStatus.MARRIED)
                .spouseInfo(null)
                .build();

        var expected = ValidationDetails.builder()
                .status(ValidationStatus.INVALID)
                .failedRules(List.of(
                        RuleResult.builder()
                                .ruleName("MARRIED_SPOUSE_INFO")
                                .message("Married student must have spouse information")
                                .build()
                ))
                .build();

        var actual = marriedSpouseInfoRule.validate(application);
        assertEquals(expected, actual);
    }

    @Test
    void singleStudentWithoutSpouseInfoIsValid() {
        var application = Application.builder()
                .maritalStatus(MaritalStatus.SINGLE)
                .spouseInfo(null)
                .build();

        var expected = ValidationDetails.builder()
                .status(ValidationStatus.VALID)
                .passedRules(List.of(
                        RuleResult.builder()
                                .ruleName("MARRIED_SPOUSE_INFO")
                                .build()
                ))
                .build();

        var actual = marriedSpouseInfoRule.validate(application);
        assertEquals(expected, actual);
    }

    @Test
    void singleStudentWithSpouseInfoIsInvalid() {
        var application = Application.builder()
                .maritalStatus(MaritalStatus.SINGLE)
                .spouseInfo(new SpouseInfo("John", "Doe", "987654321"))
                .build();

        var expected = ValidationDetails.builder()
                .status(ValidationStatus.INVALID)
                .failedRules(List.of(
                        RuleResult.builder()
                                .ruleName("MARRIED_SPOUSE_INFO")
                                .message("Single student cannot have spouse information")
                                .build()
                ))
                .build();

        var actual = marriedSpouseInfoRule.validate(application);
        assertEquals(expected, actual);
    }

    @Test
    void missingMaritalStatusIsInvalid() {
        var application = Application.builder()
                .maritalStatus(null)
                .spouseInfo(new SpouseInfo("John", "Doe", "987654321"))
                .build();

        var expected = ValidationDetails.builder()
                .status(ValidationStatus.INVALID)
                .failedRules(List.of(
                        RuleResult.builder()
                                .ruleName("MARRIED_SPOUSE_INFO")
                                .message("Marital status is missing")
                                .build()
                ))
                .build();

        var actual = marriedSpouseInfoRule.validate(application);
        assertEquals(expected, actual);
    }

    @Test
    void marriedStudentWithCompleteSpouseInfoIsValid() {
        var application = Application.builder()
                .maritalStatus(MaritalStatus.MARRIED)
                .spouseInfo(new SpouseInfo("Jane", "Smith", "123456789"))
                .build();

        var expected = ValidationDetails.builder()
                .status(ValidationStatus.VALID)
                .passedRules(List.of(
                        RuleResult.builder()
                                .ruleName("MARRIED_SPOUSE_INFO")
                                .build()
                ))
                .build();

        var actual = marriedSpouseInfoRule.validate(application);
        assertEquals(expected, actual);
    }

    @Test
    void marriedStudentWithEmptySpouseObjectIsInvalid() {
        var application = Application.builder()
                .maritalStatus(MaritalStatus.MARRIED)
                .spouseInfo(new SpouseInfo(null, null, null))
                .build();

        var expected = ValidationDetails.builder()
                .status(ValidationStatus.INVALID)
                .failedRules(List.of(
                        RuleResult.builder()
                                .ruleName("MARRIED_SPOUSE_INFO")
                                .message("Spouse first name is missing")
                                .build(),
                        RuleResult.builder()
                                .ruleName("MARRIED_SPOUSE_INFO")
                                .message("Spouse last name is missing")
                                .build(),
                        RuleResult.builder()
                                .ruleName("MARRIED_SPOUSE_INFO")
                                .message("Spouse SSN is missing")
                                .build()
                ))
                .build();

        var actual = marriedSpouseInfoRule.validate(application);
        assertEquals(expected, actual);
    }

    @Test
    void marriedStudentWithEmptyStringSpouseFieldsIsInvalid() {
        var application = Application.builder()
                .maritalStatus(MaritalStatus.MARRIED)
                .spouseInfo(new SpouseInfo("", "", ""))
                .build();

        var expected = ValidationDetails.builder()
                .status(ValidationStatus.INVALID)
                .failedRules(List.of(
                        RuleResult.builder()
                                .ruleName("MARRIED_SPOUSE_INFO")
                                .message("Spouse first name is missing")
                                .build(),
                        RuleResult.builder()
                                .ruleName("MARRIED_SPOUSE_INFO")
                                .message("Spouse last name is missing")
                                .build(),
                        RuleResult.builder()
                                .ruleName("MARRIED_SPOUSE_INFO")
                                .message("Spouse SSN is missing")
                                .build()
                ))
                .build();

        var actual = marriedSpouseInfoRule.validate(application);
        assertEquals(expected, actual);
    }

    @Test
    void marriedStudentWithMissingSpouseFirstNameIsInvalid() {
        var application = Application.builder()
                .maritalStatus(MaritalStatus.MARRIED)
                .spouseInfo(new SpouseInfo(null, "Doe", "987654321"))
                .build();

        var expected = ValidationDetails.builder()
                .status(ValidationStatus.INVALID)
                .failedRules(List.of(
                        RuleResult.builder()
                                .ruleName("MARRIED_SPOUSE_INFO")
                                .message("Spouse first name is missing")
                                .build()
                ))
                .build();

        var actual = marriedSpouseInfoRule.validate(application);
        assertEquals(expected, actual);
    }

    @Test
    void marriedStudentWithMissingSpouseLastNameIsInvalid() {
        var application = Application.builder()
                .maritalStatus(MaritalStatus.MARRIED)
                .spouseInfo(new SpouseInfo("John", null, "987654321"))
                .build();

        var expected = ValidationDetails.builder()
                .status(ValidationStatus.INVALID)
                .failedRules(List.of(
                        RuleResult.builder()
                                .ruleName("MARRIED_SPOUSE_INFO")
                                .message("Spouse last name is missing")
                                .build()
                ))
                .build();

        var actual = marriedSpouseInfoRule.validate(application);
        assertEquals(expected, actual);
    }

    @Test
    void marriedStudentWithMissingSpouseSsnIsInvalid() {
        var application = Application.builder()
                .maritalStatus(MaritalStatus.MARRIED)
                .spouseInfo(new SpouseInfo("John", "Doe", null))
                .build();

        var expected = ValidationDetails.builder()
                .status(ValidationStatus.INVALID)
                .failedRules(List.of(
                        RuleResult.builder()
                                .ruleName("MARRIED_SPOUSE_INFO")
                                .message("Spouse SSN is missing")
                                .build()
                ))
                .build();

        var actual = marriedSpouseInfoRule.validate(application);
        assertEquals(expected, actual);
    }

    @Test
    void marriedStudentWithEmptySpouseFirstNameIsInvalid() {
        try (MockedStatic<SsnValidator> mockedValidator = mockStatic(SsnValidator.class)) {
            mockedValidator.when(() -> SsnValidator.isValidSsn("987654321")).thenReturn(true);

            var application = Application.builder()
                    .maritalStatus(MaritalStatus.MARRIED)
                    .spouseInfo(new SpouseInfo("", "Doe", "987654321"))
                    .build();

            var expected = ValidationDetails.builder()
                    .status(ValidationStatus.INVALID)
                    .failedRules(List.of(
                            RuleResult.builder()
                                    .ruleName("MARRIED_SPOUSE_INFO")
                                    .message("Spouse first name is missing")
                                    .build()
                    ))
                    .build();

            var actual = marriedSpouseInfoRule.validate(application);
            assertEquals(expected, actual);
        }
    }

    @Test
    void marriedStudentWithMultipleMissingSpouseFieldsIsInvalid() {
        try (MockedStatic<SsnValidator> mockedValidator = mockStatic(SsnValidator.class)) {
            mockedValidator.when(() -> SsnValidator.isValidSsn("987654321")).thenReturn(true);

            var application = Application.builder()
                    .maritalStatus(MaritalStatus.MARRIED)
                    .spouseInfo(new SpouseInfo("", null, "987654321"))
                    .build();

            var expected = ValidationDetails.builder()
                    .status(ValidationStatus.INVALID)
                    .failedRules(List.of(
                            RuleResult.builder()
                                    .ruleName("MARRIED_SPOUSE_INFO")
                                    .message("Spouse first name is missing")
                                    .build(),
                            RuleResult.builder()
                                    .ruleName("MARRIED_SPOUSE_INFO")
                                    .message("Spouse last name is missing")
                                    .build()
                    ))
                    .build();

            var actual = marriedSpouseInfoRule.validate(application);
            assertEquals(expected, actual);
        }
    }

    @Test
    void marriedStudentWithInvalidSpouseSsnIsInvalid() {
        var application = Application.builder()
                .maritalStatus(MaritalStatus.MARRIED)
                .spouseInfo(new SpouseInfo("John", "Doe", "invalid"))
                .build();

        var expected = ValidationDetails.builder()
                .status(ValidationStatus.INVALID)
                .failedRules(List.of(
                        RuleResult.builder()
                                .ruleName("MARRIED_SPOUSE_INFO")
                                .message("Spouse SSN must be exactly 9 digits. Provided: invalid")
                                .build()
                ))
                .build();

        var actual = marriedSpouseInfoRule.validate(application);
        assertEquals(expected, actual);
    }
}
