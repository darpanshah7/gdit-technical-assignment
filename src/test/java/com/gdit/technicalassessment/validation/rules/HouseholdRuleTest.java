package com.gdit.technicalassessment.validation.rules;

import com.gdit.technicalassessment.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HouseholdRuleTest {

    private HouseholdRule householdRule;

    @BeforeEach
    void setUp() {
        householdRule = new HouseholdRule();
    }

    @Test
    void householdIsValid() {
        var application = Application.builder()
                .household(new Household(4, 1))
                .build();

        var expected = ValidationDetails.builder()
                .status(ValidationStatus.VALID)
                .passedRules(List.of(
                        RuleResult.builder()
                                .ruleName("HOUSEHOLD_COLLEGE_COUNT")
                                .build()
                ))
                .build();

        var actual = householdRule.validate(application);
        assertEquals(expected, actual);
    }

    @Test
    void householdIsValidWhenEqual() {
        var application = Application.builder()
                .household(new Household(2, 2))
                .build();

        var expected = ValidationDetails.builder()
                .status(ValidationStatus.VALID)
                .passedRules(List.of(
                        RuleResult.builder()
                                .ruleName("HOUSEHOLD_COLLEGE_COUNT")
                                .build()
                ))
                .build();

        var actual = householdRule.validate(application);
        assertEquals(expected, actual);
    }

    @Test
    void householdIsValidWhenGreaterThanCollege() {
        var application = Application.builder()
                .household(new Household(4, 0))
                .build();

        var expected = ValidationDetails.builder()
                .status(ValidationStatus.VALID)
                .passedRules(List.of(
                        RuleResult.builder()
                                .ruleName("HOUSEHOLD_COLLEGE_COUNT")
                                .build()
                ))
                .build();

        var actual = householdRule.validate(application);
        assertEquals(expected, actual);
    }

    @Test
    void householdIsValidWhenHouseholdAndCollegeAreEqual() {
        var application = Application.builder()
                .household(new Household(1, 1))
                .build();

        var expected = ValidationDetails.builder()
                .status(ValidationStatus.VALID)
                .passedRules(List.of(
                        RuleResult.builder()
                                .ruleName("HOUSEHOLD_COLLEGE_COUNT")
                                .build()
                ))
                .build();

        var actual = householdRule.validate(application);
        assertEquals(expected, actual);
    }

    @Test
    void householdInvalidWhenCollegeExceedsHousehold() {
        var application = Application.builder()
                .household(new Household(2, 3))
                .build();

        var expected = ValidationDetails.builder()
                .status(ValidationStatus.INVALID)
                .failedRules(List.of(
                        RuleResult.builder()
                                .ruleName("HOUSEHOLD_COLLEGE_COUNT")
                                .message("Number in college (3) cannot exceed number in household (2)")
                                .build()
                ))
                .build();

        var actual = householdRule.validate(application);
        assertEquals(expected, actual);
    }

    @Test
    void householdInvalidWhenInformationMissing() {
        var application = Application.builder()
                .household(null)
                .build();

        var expected = ValidationDetails.builder()
                .status(ValidationStatus.INVALID)
                .failedRules(List.of(
                        RuleResult.builder()
                                .ruleName("HOUSEHOLD_COLLEGE_COUNT")
                                .message("Household information is missing")
                                .build()
                ))
                .build();

        var actual = householdRule.validate(application);
        assertEquals(expected, actual);
    }

    @Test
    void householdInvalidWhenNumberInHouseholdMissing() {
        var application = Application.builder()
                .household(new Household(null, 2))
                .build();

        var expected = ValidationDetails.builder()
                .status(ValidationStatus.INVALID)
                .failedRules(List.of(
                        RuleResult.builder()
                                .ruleName("HOUSEHOLD_COLLEGE_COUNT")
                                .message("Number in household is missing")
                                .build()
                ))
                .build();

        var actual = householdRule.validate(application);
        assertEquals(expected, actual);
    }

    @Test
    void householdInvalidWhenNumberInCollegeMissing() {
        var application = Application.builder()
                .household(new Household(4, null))
                .build();

        var expected = ValidationDetails.builder()
                .status(ValidationStatus.INVALID)
                .failedRules(List.of(
                        RuleResult.builder()
                                .ruleName("HOUSEHOLD_COLLEGE_COUNT")
                                .message("Number in college is missing")
                                .build()
                ))
                .build();

        var actual = householdRule.validate(application);
        assertEquals(expected, actual);
    }

    @Test
    void householdInvalidWhenBothValuesMissing() {
        var application = Application.builder()
                .household(new Household(null, null))
                .build();

        var expected = ValidationDetails.builder()
                .status(ValidationStatus.INVALID)
                .failedRules(List.of(
                        RuleResult.builder()
                                .ruleName("HOUSEHOLD_COLLEGE_COUNT")
                                .message("Number in household is missing")
                                .build(),
                        RuleResult.builder()
                                .ruleName("HOUSEHOLD_COLLEGE_COUNT")
                                .message("Number in college is missing")
                                .build()
                ))
                .build();

        var actual = householdRule.validate(application);
        assertEquals(expected, actual);
    }


    @Test
    void householdInvalidWhenCollegeIsZeroAndHouseholdNegative() {
        var application = Application.builder()
                .household(new Household(-1, 0))
                .build();

        var expected = ValidationDetails.builder()
                .status(ValidationStatus.INVALID)
                .failedRules(List.of(
                        RuleResult.builder()
                                .ruleName("HOUSEHOLD_COLLEGE_COUNT")
                                .message("Number in household cannot be negative. Provided: -1")
                                .build()
                ))
                .build();

        var actual = householdRule.validate(application);
        assertEquals(expected, actual);
    }

    @Test
    void householdInvalidWhenBothNegative() {
        var application = Application.builder()
                .household(new Household(-5, -2))
                .build();

        var expected = ValidationDetails.builder()
                .status(ValidationStatus.INVALID)
                .failedRules(List.of(
                        RuleResult.builder()
                                .ruleName("HOUSEHOLD_COLLEGE_COUNT")
                                .message("Number in household cannot be negative. Provided: -5")
                                .build(),
                        RuleResult.builder()
                                .ruleName("HOUSEHOLD_COLLEGE_COUNT")
                                .message("Number in college cannot be negative. Provided: -2")
                                .build()
                ))
                .build();

        var actual = householdRule.validate(application);
        assertEquals(expected, actual);
    }

    @Test
    void householdInvalidWhenCollegeIsNegative() {
        var application = Application.builder()
                .household(new Household(5, -1))
                .build();

        var expected = ValidationDetails.builder()
                .status(ValidationStatus.INVALID)
                .failedRules(List.of(
                        RuleResult.builder()
                                .ruleName("HOUSEHOLD_COLLEGE_COUNT")
                                .message("Number in college cannot be negative. Provided: -1")
                                .build()
                ))
                .build();

        var actual = householdRule.validate(application);
        assertEquals(expected, actual);
    }
}

