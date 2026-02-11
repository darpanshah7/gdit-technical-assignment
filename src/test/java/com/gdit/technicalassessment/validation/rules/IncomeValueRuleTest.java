package com.gdit.technicalassessment.validation.rules;

import com.gdit.technicalassessment.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class IncomeValueRuleTest {

    private IncomeValueRule incomeValueRule;

    @BeforeEach
    void setUp() {
        incomeValueRule = new IncomeValueRule();
    }

    @Test
    void incomeIsValid() {
        var application = Application.builder()
                .income(new Income(100, 1000))
                .build();

        var expected = ValidationDetails.builder()
                .status(ValidationStatus.VALID)
                .passedRules(List.of(
                        RuleResult.builder()
                                .ruleName("INCOME_VALUE")
                                .build()
                ))
                .build();

        var actual = incomeValueRule.validate(application);
        assertEquals(expected, actual);
    }

    @Test
    void incomeIsValidWithZeroValues() {
        var application = Application.builder()
                .income(new Income(0, 0))
                .build();

        var expected = ValidationDetails.builder()
                .status(ValidationStatus.VALID)
                .passedRules(List.of(
                        RuleResult.builder()
                                .ruleName("INCOME_VALUE")
                                .build()
                ))
                .build();

        var actual = incomeValueRule.validate(application);
        assertEquals(expected, actual);
    }

    @Test
    void incomeIsValidWithOnlyStudentIncome() {
        var application = Application.builder()
                .income(new Income(5000, null))
                .build();

        var expected = ValidationDetails.builder()
                .status(ValidationStatus.VALID)
                .passedRules(List.of(
                        RuleResult.builder()
                                .ruleName("INCOME_VALUE")
                                .build()
                ))
                .build();

        var actual = incomeValueRule.validate(application);
        assertEquals(expected, actual);
    }

    @Test
    void incomeIsValidWithOnlyParentIncome() {
        var application = Application.builder()
                .income(new Income(null, 65000))
                .build();

        var expected = ValidationDetails.builder()
                .status(ValidationStatus.VALID)
                .passedRules(List.of(
                        RuleResult.builder()
                                .ruleName("INCOME_VALUE")
                                .build()
                ))
                .build();

        var actual = incomeValueRule.validate(application);
        assertEquals(expected, actual);
    }

    @Test
    void studentIncomeIsNegative() {
        var application = Application.builder()
                .income(new Income(-1, 1))
                .build();

        var expected = ValidationDetails.builder()
                .status(ValidationStatus.INVALID)
                .failedRules(List.of(
                        RuleResult.builder()
                                .ruleName("INCOME_VALUE")
                                .message("Student income cannot be negative. Provided: -1")
                                .build()
                ))
                .build();

        var actual = incomeValueRule.validate(application);
        assertEquals(expected, actual);
    }

    @Test
    void parentIncomeIsNegative() {
        var application = Application.builder()
                .income(new Income(1, -1))
                .build();

        var expected = ValidationDetails.builder()
                .status(ValidationStatus.INVALID)
                .failedRules(List.of(
                        RuleResult.builder()
                                .ruleName("INCOME_VALUE")
                                .message("Parent income cannot be negative. Provided: -1")
                                .build()
                ))
                .build();

        var actual = incomeValueRule.validate(application);
        assertEquals(expected, actual);
    }

    @Test
    void bothIncomesAreNegative() {
        var application = Application.builder()
                .income(new Income(-1, -2))
                .build();

        var expected = ValidationDetails.builder()
                .status(ValidationStatus.INVALID)
                .failedRules(List.of(
                        RuleResult.builder()
                                .ruleName("INCOME_VALUE")
                                .message("Student income cannot be negative. Provided: -1")
                                .build(),
                        RuleResult.builder()
                                .ruleName("INCOME_VALUE")
                                .message("Parent income cannot be negative. Provided: -2")
                                .build()
                ))
                .build();

        var actual = incomeValueRule.validate(application);
        assertEquals(expected, actual);
    }

    @Test
    void incomeInformationIsMissing() {
        var application = Application.builder()
                .income(null)
                .build();

        var expected = ValidationDetails.builder()
                .status(ValidationStatus.INVALID)
                .failedRules(List.of(
                        RuleResult.builder()
                                .ruleName("INCOME_VALUE")
                                .message("Income information is missing")
                                .build()
                ))
                .build();

        var actual = incomeValueRule.validate(application);
        assertEquals(expected, actual);
    }

    @Test
    void incomeIsValidWithBothNullValues() {
        var application = Application.builder()
                .income(new Income(null, null))
                .build();

        var expected = ValidationDetails.builder()
                .status(ValidationStatus.VALID)
                .passedRules(List.of(
                        RuleResult.builder()
                                .ruleName("INCOME_VALUE")
                                .build()
                ))
                .build();

        var actual = incomeValueRule.validate(application);
        assertEquals(expected, actual);
    }

    @Test
    void studentIncomeIsNegativeWithNullParentIncome() {
        var application = Application.builder()
                .income(new Income(-1, null))
                .build();

        var expected = ValidationDetails.builder()
                .status(ValidationStatus.INVALID)
                .failedRules(List.of(
                        RuleResult.builder()
                                .ruleName("INCOME_VALUE")
                                .message("Student income cannot be negative. Provided: -1")
                                .build()
                ))
                .build();

        var actual = incomeValueRule.validate(application);
        assertEquals(expected, actual);
    }

    @Test
    void parentIncomeIsNegativeWithNullStudentIncome() {
        var application = Application.builder()
                .income(new Income(null, -1))
                .build();

        var expected = ValidationDetails.builder()
                .status(ValidationStatus.INVALID)
                .failedRules(List.of(
                        RuleResult.builder()
                                .ruleName("INCOME_VALUE")
                                .message("Parent income cannot be negative. Provided: -1")
                                .build()
                ))
                .build();

        var actual = incomeValueRule.validate(application);
        assertEquals(expected, actual);
    }

    @Test
    void incomeIsValidWithLargeValues() {
        var application = Application.builder()
                .income(new Income(100000, 500000))
                .build();

        var expected = ValidationDetails.builder()
                .status(ValidationStatus.VALID)
                .passedRules(List.of(
                        RuleResult.builder()
                                .ruleName("INCOME_VALUE")
                                .build()
                ))
                .build();

        var actual = incomeValueRule.validate(application);
        assertEquals(expected, actual);
    }
}

