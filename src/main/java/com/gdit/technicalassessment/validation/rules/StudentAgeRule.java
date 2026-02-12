package com.gdit.technicalassessment.validation.rules;

import com.gdit.technicalassessment.model.Application;
import com.gdit.technicalassessment.model.RuleResult;
import com.gdit.technicalassessment.model.ValidationDetails;
import com.gdit.technicalassessment.model.ValidationStatus;
import com.gdit.technicalassessment.validation.ValidationRule;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@Component
public class StudentAgeRule implements ValidationRule {

    private static final int MINIMUM_AGE = 14;
    private final Clock clock;

    public StudentAgeRule() {
        this(Clock.systemUTC());
    }

    public StudentAgeRule(Clock clock) {
        this.clock = clock;
    }

    @Override
    public String getRuleName() {
        return "STUDENT_AGE";
    }

    @Override
    public boolean isActive() {
        return true;
    }

    @Override
    public ValidationDetails validate(Application application) {
        if (!hasDateOfBirth(application)) {
            return buildInvalidResult("Student date of birth is missing");
        }

        LocalDate dateOfBirth = application.studentInfo().dateOfBirth();
        LocalDate now = LocalDate.now(clock);

        if (dateOfBirth.isAfter(now)) {
            return buildInvalidResult(String.format("Date of birth cannot be in the future: %s", dateOfBirth));
        }

        int age = calculateAge(dateOfBirth, now);

        if (isBelowMinimumAge(age)) {
            return buildInvalidResult(String.format(
                    "Student must be at least %d years old. Current age: %d", MINIMUM_AGE, age));
        }

        return ValidationDetails.builder()
                .status(ValidationStatus.VALID)
                .passedRules(List.of(buildRuleResult(null)))
                .build();
    }

    private boolean hasDateOfBirth(Application application) {
        return application.studentInfo() != null && application.studentInfo().dateOfBirth() != null;
    }

    private int calculateAge(LocalDate dateOfBirth, LocalDate now) {
        return Period.between(dateOfBirth, now).getYears();
    }

    private boolean isBelowMinimumAge(int age) {
        return age < MINIMUM_AGE;
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

