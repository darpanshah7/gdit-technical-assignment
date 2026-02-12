package com.gdit.technicalassessment.service;

import com.gdit.technicalassessment.model.*;
import com.gdit.technicalassessment.validation.ValidationRule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ApplicationValidationServiceTest {

    private ApplicationValidationService service;
    private ValidationRule mockRule1;
    private ValidationRule mockRule2;
    private ValidationRule mockRule3;
    private Application testApplication;

    @BeforeEach
    void setUp() {
        mockRule1 = mock(ValidationRule.class);
        mockRule2 = mock(ValidationRule.class);
        mockRule3 = mock(ValidationRule.class);

        when(mockRule1.getRuleName()).thenReturn("RULE_1");
        when(mockRule2.getRuleName()).thenReturn("RULE_2");
        when(mockRule3.getRuleName()).thenReturn("RULE_3");

        when(mockRule1.isActive()).thenReturn(true);
        when(mockRule2.isActive()).thenReturn(true);
        when(mockRule3.isActive()).thenReturn(true);

        testApplication = Application.builder()
                .studentInfo(StudentInfo.builder()
                        .firstName("Jane")
                        .lastName("Smith")
                        .ssn("123456789")
                        .dateOfBirth(LocalDate.of(2003, 5, 15))
                        .build())
                .dependencyStatus(DependencyStatus.DEPENDENT)
                .maritalStatus(MaritalStatus.SINGLE)
                .build();
    }

    @Test
    void shouldReturnValidWhenAllRulesPass() {
        ValidationDetails rule1Result = ValidationDetails.builder()
                .status(ValidationStatus.VALID)
                .passedRules(List.of(RuleResult.builder().ruleName("RULE_1").build()))
                .build();

        ValidationDetails rule2Result = ValidationDetails.builder()
                .status(ValidationStatus.VALID)
                .passedRules(List.of(RuleResult.builder().ruleName("RULE_2").build()))
                .build();

        when(mockRule1.validate(testApplication)).thenReturn(rule1Result);
        when(mockRule2.validate(testApplication)).thenReturn(rule2Result);

        service = new ApplicationValidationService(Arrays.asList(mockRule1, mockRule2));

        ValidationDetails result = service.validateApplication(testApplication);

        assertEquals(ValidationStatus.VALID, result.status());
        assertEquals(2, result.passedRules().size());
        assertTrue(result.failedRules().isEmpty());
        verify(mockRule1).validate(testApplication);
        verify(mockRule2).validate(testApplication);
    }

    @Test
    void shouldReturnInvalidWhenOneRuleFails() {
        ValidationDetails rule1Result = ValidationDetails.builder()
                .status(ValidationStatus.VALID)
                .passedRules(List.of(RuleResult.builder().ruleName("RULE_1").build()))
                .build();

        ValidationDetails rule2Result = ValidationDetails.builder()
                .status(ValidationStatus.INVALID)
                .failedRules(List.of(RuleResult.builder()
                        .ruleName("RULE_2")
                        .message("Rule 2 failed")
                        .build()))
                .build();

        when(mockRule1.validate(testApplication)).thenReturn(rule1Result);
        when(mockRule2.validate(testApplication)).thenReturn(rule2Result);

        service = new ApplicationValidationService(Arrays.asList(mockRule1, mockRule2));

        ValidationDetails result = service.validateApplication(testApplication);

        assertEquals(ValidationStatus.INVALID, result.status());
        assertEquals(1, result.passedRules().size());
        assertEquals(1, result.failedRules().size());
        assertEquals("RULE_2", result.failedRules().get(0).ruleName());
        assertEquals("Rule 2 failed", result.failedRules().get(0).message());
    }

    @Test
    void shouldReturnInvalidWhenAllRulesFail() {
        ValidationDetails rule1Result = ValidationDetails.builder()
                .status(ValidationStatus.INVALID)
                .failedRules(List.of(RuleResult.builder()
                        .ruleName("RULE_1")
                        .message("Rule 1 failed")
                        .build()))
                .build();

        ValidationDetails rule2Result = ValidationDetails.builder()
                .status(ValidationStatus.INVALID)
                .failedRules(List.of(RuleResult.builder()
                        .ruleName("RULE_2")
                        .message("Rule 2 failed")
                        .build()))
                .build();

        when(mockRule1.validate(testApplication)).thenReturn(rule1Result);
        when(mockRule2.validate(testApplication)).thenReturn(rule2Result);

        service = new ApplicationValidationService(Arrays.asList(mockRule1, mockRule2));

        ValidationDetails result = service.validateApplication(testApplication);

        assertEquals(ValidationStatus.INVALID, result.status());
        assertTrue(result.passedRules().isEmpty());
        assertEquals(2, result.failedRules().size());
    }

    @Test
    void shouldAccumulateMultipleFailuresFromDifferentRules() {
        ValidationDetails rule1Result = ValidationDetails.builder()
                .status(ValidationStatus.INVALID)
                .failedRules(Arrays.asList(
                        RuleResult.builder().ruleName("RULE_1").message("Error 1a").build(),
                        RuleResult.builder().ruleName("RULE_1").message("Error 1b").build()
                ))
                .build();

        ValidationDetails rule2Result = ValidationDetails.builder()
                .status(ValidationStatus.INVALID)
                .failedRules(List.of(RuleResult.builder()
                        .ruleName("RULE_2")
                        .message("Error 2")
                        .build()))
                .build();

        when(mockRule1.validate(testApplication)).thenReturn(rule1Result);
        when(mockRule2.validate(testApplication)).thenReturn(rule2Result);

        service = new ApplicationValidationService(Arrays.asList(mockRule1, mockRule2));

        ValidationDetails result = service.validateApplication(testApplication);

        assertEquals(ValidationStatus.INVALID, result.status());
        assertEquals(3, result.failedRules().size());
    }

    @Test
    void shouldSkipInactiveRules() {
        when(mockRule1.isActive()).thenReturn(true);
        when(mockRule2.isActive()).thenReturn(false);

        ValidationDetails rule1Result = ValidationDetails.builder()
                .status(ValidationStatus.VALID)
                .passedRules(List.of(RuleResult.builder().ruleName("RULE_1").build()))
                .build();

        when(mockRule1.validate(testApplication)).thenReturn(rule1Result);

        service = new ApplicationValidationService(Arrays.asList(mockRule1, mockRule2));

        ValidationDetails result = service.validateApplication(testApplication);

        assertEquals(ValidationStatus.VALID, result.status());
        assertEquals(1, result.passedRules().size());
        verify(mockRule1).validate(testApplication);
        verify(mockRule2, never()).validate(any());
    }

    @Test
    void shouldHandleRulesWithNullPassedRulesList() {
        ValidationDetails rule1Result = ValidationDetails.builder()
                .status(ValidationStatus.VALID)
                .passedRules(null)
                .build();

        when(mockRule1.validate(testApplication)).thenReturn(rule1Result);

        service = new ApplicationValidationService(List.of(mockRule1));

        ValidationDetails result = service.validateApplication(testApplication);

        assertEquals(ValidationStatus.VALID, result.status());
        assertTrue(result.passedRules().isEmpty());
    }

    @Test
    void shouldHandleRulesWithNullFailedRulesList() {
        ValidationDetails rule1Result = ValidationDetails.builder()
                .status(ValidationStatus.VALID)
                .passedRules(List.of(RuleResult.builder().ruleName("RULE_1").build()))
                .failedRules(null)
                .build();

        when(mockRule1.validate(testApplication)).thenReturn(rule1Result);

        service = new ApplicationValidationService(List.of(mockRule1));

        ValidationDetails result = service.validateApplication(testApplication);

        assertEquals(ValidationStatus.VALID, result.status());
        assertTrue(result.failedRules().isEmpty());
    }

    @Test
    void shouldHandleEmptyRulesCollection() {
        service = new ApplicationValidationService(Collections.emptyList());

        ValidationDetails result = service.validateApplication(testApplication);

        assertEquals(ValidationStatus.VALID, result.status());
        assertTrue(result.passedRules().isEmpty());
        assertTrue(result.failedRules().isEmpty());
    }

    @Test
    void shouldApplyAllRulesEvenIfSomeFail() {
        ValidationDetails rule1Result = ValidationDetails.builder()
                .status(ValidationStatus.INVALID)
                .failedRules(List.of(RuleResult.builder()
                        .ruleName("RULE_1")
                        .message("Rule 1 failed")
                        .build()))
                .build();

        ValidationDetails rule2Result = ValidationDetails.builder()
                .status(ValidationStatus.VALID)
                .passedRules(List.of(RuleResult.builder().ruleName("RULE_2").build()))
                .build();

        ValidationDetails rule3Result = ValidationDetails.builder()
                .status(ValidationStatus.INVALID)
                .failedRules(List.of(RuleResult.builder()
                        .ruleName("RULE_3")
                        .message("Rule 3 failed")
                        .build()))
                .build();

        when(mockRule1.validate(testApplication)).thenReturn(rule1Result);
        when(mockRule2.validate(testApplication)).thenReturn(rule2Result);
        when(mockRule3.validate(testApplication)).thenReturn(rule3Result);

        service = new ApplicationValidationService(Arrays.asList(mockRule1, mockRule2, mockRule3));

        ValidationDetails result = service.validateApplication(testApplication);

        assertEquals(ValidationStatus.INVALID, result.status());
        assertEquals(1, result.passedRules().size());
        assertEquals(2, result.failedRules().size());
        verify(mockRule1).validate(testApplication);
        verify(mockRule2).validate(testApplication);
        verify(mockRule3).validate(testApplication);
    }

    @Test
    void shouldHandleRuleWithBothPassedAndFailedResults() {
        ValidationDetails rule1Result = ValidationDetails.builder()
                .status(ValidationStatus.INVALID)
                .passedRules(List.of(RuleResult.builder().ruleName("RULE_1").message("Check A passed").build()))
                .failedRules(List.of(RuleResult.builder().ruleName("RULE_1").message("Check B failed").build()))
                .build();

        when(mockRule1.validate(testApplication)).thenReturn(rule1Result);

        service = new ApplicationValidationService(List.of(mockRule1));

        ValidationDetails result = service.validateApplication(testApplication);

        assertEquals(ValidationStatus.INVALID, result.status());
        assertEquals(1, result.passedRules().size());
        assertEquals(1, result.failedRules().size());
    }

    @Test
    void shouldPreserveRuleOrderInResults() {
        ValidationDetails rule1Result = ValidationDetails.builder()
                .status(ValidationStatus.INVALID)
                .failedRules(List.of(RuleResult.builder().ruleName("RULE_1").message("First error").build()))
                .build();

        ValidationDetails rule2Result = ValidationDetails.builder()
                .status(ValidationStatus.INVALID)
                .failedRules(List.of(RuleResult.builder().ruleName("RULE_2").message("Second error").build()))
                .build();

        ValidationDetails rule3Result = ValidationDetails.builder()
                .status(ValidationStatus.INVALID)
                .failedRules(List.of(RuleResult.builder().ruleName("RULE_3").message("Third error").build()))
                .build();

        when(mockRule1.validate(testApplication)).thenReturn(rule1Result);
        when(mockRule2.validate(testApplication)).thenReturn(rule2Result);
        when(mockRule3.validate(testApplication)).thenReturn(rule3Result);

        service = new ApplicationValidationService(Arrays.asList(mockRule1, mockRule2, mockRule3));

        ValidationDetails result = service.validateApplication(testApplication);

        assertEquals(3, result.failedRules().size());
        assertEquals("RULE_1", result.failedRules().get(0).ruleName());
        assertEquals("RULE_2", result.failedRules().get(1).ruleName());
        assertEquals("RULE_3", result.failedRules().get(2).ruleName());
    }

    @Test
    void shouldInitializeWithCorrectNumberOfRules() {
        service = new ApplicationValidationService(Arrays.asList(mockRule1, mockRule2, mockRule3));

        assertNotNull(service);
    }

    @Test
    void shouldHandleComplexApplicationWithMultipleValidationResults() {
        Application complexApp = Application.builder()
                .studentInfo(StudentInfo.builder()
                        .firstName("John")
                        .lastName("Doe")
                        .ssn("987654321")
                        .dateOfBirth(LocalDate.of(2000, 1, 1))
                        .build())
                .dependencyStatus(DependencyStatus.INDEPENDENT)
                .maritalStatus(MaritalStatus.MARRIED)
                .spouseInfo(new SpouseInfo("Jane", "Doe", "123456789"))
                .household(new Household(3, 2))
                .income(new Income(30000, null))
                .stateOfResidence("NY")
                .build();

        ValidationDetails rule1Result = ValidationDetails.builder()
                .status(ValidationStatus.VALID)
                .passedRules(List.of(RuleResult.builder().ruleName("RULE_1").build()))
                .build();

        ValidationDetails rule2Result = ValidationDetails.builder()
                .status(ValidationStatus.VALID)
                .passedRules(List.of(RuleResult.builder().ruleName("RULE_2").build()))
                .build();

        when(mockRule1.validate(complexApp)).thenReturn(rule1Result);
        when(mockRule2.validate(complexApp)).thenReturn(rule2Result);

        service = new ApplicationValidationService(Arrays.asList(mockRule1, mockRule2));

        ValidationDetails result = service.validateApplication(complexApp);

        assertEquals(ValidationStatus.VALID, result.status());
        assertEquals(2, result.passedRules().size());
        assertTrue(result.failedRules().isEmpty());
    }
}

