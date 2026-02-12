package com.gdit.technicalassessment.validation.rules;

import com.gdit.technicalassessment.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DependentParentIncomeRuleTest {

    private DependentParentIncomeRule dependentParentIncomeRule;

    @BeforeEach
    void setUp() {
        dependentParentIncomeRule = new DependentParentIncomeRule();
    }

    @Test
    void dependentStudentWithParentIncomeIsValid() {
        var application = Application.builder()
                .dependencyStatus("DEPENDENT")
                .income(new Income(1, 2))
                .build();

        var expected = ValidationDetails.builder()
                .status(ValidationStatus.VALID)
                .passedRules(List.of(
                        RuleResult.builder()
                                .ruleName("DEPENDENT_PARENT_INCOME")
                                .build()
                ))
                .build();

        var actual = dependentParentIncomeRule.validate(application);
        assertEquals(expected, actual);
    }

    @Test
    void dependentStudentWithoutParentIncomeIsInvalid() {
        var application = Application.builder()
                .dependencyStatus("DEPENDENT")
                .income(new Income(1, null))
                .build();

        var expected = ValidationDetails.builder()
                .status(ValidationStatus.INVALID)
                .failedRules(List.of(
                        RuleResult.builder()
                                .ruleName("DEPENDENT_PARENT_INCOME")
                                .message("Dependent student must have parent income")
                                .build()
                ))
                .build();

        var actual = dependentParentIncomeRule.validate(application);
        assertEquals(expected, actual);
    }

    @Test
    void dependentStudentWithoutIncomeInformationIsInvalid() {
        var application = Application.builder()
                .dependencyStatus("DEPENDENT")
                .income(null)
                .build();

        var expected = ValidationDetails.builder()
                .status(ValidationStatus.INVALID)
                .failedRules(List.of(
                        RuleResult.builder()
                                .ruleName("DEPENDENT_PARENT_INCOME")
                                .message("Dependent student must have income information with parent income")
                                .build()
                ))
                .build();

        var actual = dependentParentIncomeRule.validate(application);
        assertEquals(expected, actual);
    }

    @Test
    void independentStudentWithoutParentIncomeIsValid() {
        var application = Application.builder()
                .dependencyStatus("INDEPENDENT")
                .income(new Income(1, null))
                .build();

        var expected = ValidationDetails.builder()
                .status(ValidationStatus.VALID)
                .passedRules(List.of(
                        RuleResult.builder()
                                .ruleName("DEPENDENT_PARENT_INCOME")
                                .build()
                ))
                .build();

        var actual = dependentParentIncomeRule.validate(application);
        assertEquals(expected, actual);
    }

    @Test
    void independentStudentWithParentIncomeIsValid() {
        var application = Application.builder()
                .dependencyStatus("INDEPENDENT")
                .income(new Income(1, 2))
                .build();

        var expected = ValidationDetails.builder()
                .status(ValidationStatus.VALID)
                .passedRules(List.of(
                        RuleResult.builder()
                                .ruleName("DEPENDENT_PARENT_INCOME")
                                .build()
                ))
                .build();

        var actual = dependentParentIncomeRule.validate(application);
        assertEquals(expected, actual);
    }

    @Test
    void missingDependencyStatusIsInvalid() {
        var application = Application.builder()
                .dependencyStatus(null)
                .income(new Income(1, 2))
                .build();

        var expected = ValidationDetails.builder()
                .status(ValidationStatus.INVALID)
                .failedRules(List.of(
                        RuleResult.builder()
                                .ruleName("DEPENDENT_PARENT_INCOME")
                                .message("Dependency status is missing")
                                .build()
                ))
                .build();

        var actual = dependentParentIncomeRule.validate(application);
        assertEquals(expected, actual);
    }

    @Test
    void invalidDependencyStatusIsInvalid() {
        var application = Application.builder()
                .dependencyStatus("INVALID_STATUS")
                .income(new Income(1, 2))
                .build();

        var expected = ValidationDetails.builder()
                .status(ValidationStatus.INVALID)
                .failedRules(List.of(
                        RuleResult.builder()
                                .ruleName("DEPENDENT_PARENT_INCOME")
                                .message("Invalid dependency status. Must be one of the following: [DEPENDENT, INDEPENDENT], Provided: INVALID_STATUS")
                                .build()
                ))
                .build();

        var actual = dependentParentIncomeRule.validate(application);
        assertEquals(expected, actual);
    }

    @Test
    void dependentStudentWithNullIncomeValuesIsInvalid() {
        var application = Application.builder()
                .dependencyStatus("DEPENDENT")
                .income(new Income(null, null))
                .build();

        var expected = ValidationDetails.builder()
                .status(ValidationStatus.INVALID)
                .failedRules(List.of(
                        RuleResult.builder()
                                .ruleName("DEPENDENT_PARENT_INCOME")
                                .message("Dependent student must have parent income")
                                .build()
                ))
                .build();

        var actual = dependentParentIncomeRule.validate(application);
        assertEquals(expected, actual);
    }
}
