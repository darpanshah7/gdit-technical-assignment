package com.gdit.technicalassessment.validation.rules;

import com.gdit.technicalassessment.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StudentAgeRuleTest {

    private static final ZoneId ZONE_ID = ZoneId.of("UTC");
    private StudentAgeRule studentAgeRule;
    private Clock fixedClock;

    @BeforeEach
    void setUp() {
        LocalDate fixedDate = LocalDate.of(2026, 1, 1);
        Instant fixedInstant = fixedDate.atStartOfDay(ZONE_ID).toInstant();
        fixedClock = Clock.fixed(fixedInstant, ZONE_ID);
        studentAgeRule = new StudentAgeRule(fixedClock);
    }

    @Test
    void studentAgeIsValidWhenAboveMinimumAge() {
        var application = Application.builder()
                .studentInfo(StudentInfo.builder()
                        .dateOfBirth(LocalDate.of(2000, 1, 1))
                        .build())
                .build();

        var expected = ValidationDetails.builder()
                .status(ValidationStatus.VALID)
                .passedRules(List.of(
                        RuleResult.builder()
                                .ruleName("STUDENT_AGE")
                                .build()
                ))
                .build();

        var actual = studentAgeRule.validate(application);
        assertEquals(expected, actual);
    }

    @Test
    void studentAgeIsValidWhenExactlyMinimumAge() {
        var application = Application.builder()
                .studentInfo(StudentInfo.builder()
                        .dateOfBirth(LocalDate.of(2012, 1, 1))
                        .build())
                .build();

        var expected = ValidationDetails.builder()
                .status(ValidationStatus.VALID)
                .passedRules(List.of(
                        RuleResult.builder()
                                .ruleName("STUDENT_AGE")
                                .build()
                ))
                .build();

        var actual = studentAgeRule.validate(application);
        assertEquals(expected, actual);
    }

    @Test
    void studentAgeIsInvalidWhenBelowMinimumAge() {
        var application = Application.builder()
                .studentInfo(StudentInfo.builder()
                        .dateOfBirth(LocalDate.of(2013, 1, 1))
                        .build())
                .build();

        var expected = ValidationDetails.builder()
                .status(ValidationStatus.INVALID)
                .failedRules(List.of(
                        RuleResult.builder()
                                .ruleName("STUDENT_AGE")
                                .message("Student must be at least 14 years old. Current age: 13")
                                .build()
                ))
                .build();

        var actual = studentAgeRule.validate(application);
        assertEquals(expected, actual);
    }

    @Test
    void studentDateOfBirthIsInvalidWhenMissing() {
        var application = Application.builder()
                .studentInfo(StudentInfo.builder()
                        .dateOfBirth(null)
                        .build())
                .build();

        var expected = ValidationDetails.builder()
                .status(ValidationStatus.INVALID)
                .failedRules(List.of(
                        RuleResult.builder()
                                .ruleName("STUDENT_AGE")
                                .message("Student date of birth is missing")
                                .build()
                ))
                .build();

        var actual = studentAgeRule.validate(application);
        assertEquals(expected, actual);
    }

    @Test
    void isInvalidWhenStudentInfoIsMissing() {
        var application = Application.builder()
                .studentInfo(null)
                .build();

        var expected = ValidationDetails.builder()
                .status(ValidationStatus.INVALID)
                .failedRules(List.of(
                        RuleResult.builder()
                                .ruleName("STUDENT_AGE")
                                .message("Student date of birth is missing")
                                .build()
                ))
                .build();

        var actual = studentAgeRule.validate(application);
        assertEquals(expected, actual);
    }

    @Test
    void studentDateOfBirthIsInvalidWhenInFuture() {
        var application = Application.builder()
                .studentInfo(StudentInfo.builder()
                        .dateOfBirth(LocalDate.of(2030, 1, 1))
                        .build())
                .build();

        var expected = ValidationDetails.builder()
                .status(ValidationStatus.INVALID)
                .failedRules(List.of(
                        RuleResult.builder()
                                .ruleName("STUDENT_AGE")
                                .message("Date of birth cannot be in the future: 2030-01-01")
                                .build()
                ))
                .build();

        var actual = studentAgeRule.validate(application);
        assertEquals(expected, actual);
    }

    @Test
    void studentAgeIsInvalidWhenOneDayBeforeMinimum() {
        var application = Application.builder()
                .studentInfo(StudentInfo.builder()
                        .dateOfBirth(LocalDate.of(2012, 1, 2))
                        .build())
                .build();

        var expected = ValidationDetails.builder()
                .status(ValidationStatus.INVALID)
                .failedRules(List.of(
                        RuleResult.builder()
                                .ruleName("STUDENT_AGE")
                                .message("Student must be at least 14 years old. Current age: 13")
                                .build()
                ))
                .build();

        var actual = studentAgeRule.validate(application);
        assertEquals(expected, actual);
    }
}

