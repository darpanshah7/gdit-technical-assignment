package com.gdit.technicalassessment.validation.util;

import com.gdit.technicalassessment.model.DependencyStatus;
import com.gdit.technicalassessment.model.MaritalStatus;
import com.gdit.technicalassessment.model.UsaState;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class EnumValidatorTest {

    @Nested
    class DependencyStatusValidation {

        @ParameterizedTest(name = "Should accept ''{0}'' as valid DependencyStatus")
        @ValueSource(strings = {"DEPENDENT", "dependent", "Dependent", "DePeNdEnT", "  DEPENDENT  ", "\tDEPENDENT\n"})
        void shouldAcceptDependentInAnyCase(String value) {
            assertTrue(EnumValidator.isValidEnum(value, DependencyStatus.class));
        }

        @ParameterizedTest(name = "Should accept ''{0}'' as valid DependencyStatus")
        @ValueSource(strings = {"INDEPENDENT", "independent", "Independent", "InDePeNdEnT", "  independent  ", "\tinDEPENDENT\n"})
        void shouldAcceptIndependentInAnyCase(String value) {
            assertTrue(EnumValidator.isValidEnum(value, DependencyStatus.class));
        }

        @Test
        void shouldRejectNull() {
            assertFalse(EnumValidator.isValidEnum(null, DependencyStatus.class));
        }

        @Test
        void shouldRejectEmptyString() {
            assertFalse(EnumValidator.isValidEnum("", DependencyStatus.class));
        }

        @Test
        void shouldRejectInvalidValue() {
            assertFalse(EnumValidator.isValidEnum("invalid", DependencyStatus.class));
        }

        @Test
        void shouldRejectPartialValue() {
            assertFalse(EnumValidator.isValidEnum("DEP", DependencyStatus.class));
        }
    }

    @Nested
    class MaritalStatusValidation {

        @ParameterizedTest(name = "Should accept ''{0}'' as valid MaritalStatus")
        @ValueSource(strings = {"SINGLE", "single", "Single", "SiNgLe", "  SINGLE  ", "\tSINGLE\n"})
        void shouldAcceptSingleInAnyCase(String value) {
            assertTrue(EnumValidator.isValidEnum(value, MaritalStatus.class));
        }

        @ParameterizedTest(name = "Should accept ''{0}'' as valid MaritalStatus")
        @ValueSource(strings = {"MARRIED", "married", "Married", "MaRrIeD", "  married  ", "\tmarried\n"})
        void shouldAcceptMarriedInAnyCase(String value) {
            assertTrue(EnumValidator.isValidEnum(value, MaritalStatus.class));
        }

        @Test
        void shouldRejectNull() {
            assertFalse(EnumValidator.isValidEnum(null, MaritalStatus.class));
        }

        @Test
        void shouldRejectEmptyString() {
            assertFalse(EnumValidator.isValidEnum("", MaritalStatus.class));
        }

        @Test
        void shouldRejectInvalidValue() {
            assertFalse(EnumValidator.isValidEnum("invalid", MaritalStatus.class));
        }

        @Test
        void shouldRejectPartialValue() {
            assertFalse(EnumValidator.isValidEnum("SING", MaritalStatus.class));
        }


    }

    @Nested
    class UsaStateValidation {

        @ParameterizedTest(name = "Should accept ''{0}'' as valid state code")
        @ValueSource(strings = {"CA", "NY", "TX", "FL", "MI", "DC"})
        void shouldAcceptValidStateCodesUppercase(String state) {
            assertTrue(EnumValidator.isValidEnum(state, UsaState.class));
        }

        @ParameterizedTest(name = "Should accept ''{0}'' as valid state code")
        @ValueSource(strings = {"ca", "ny", "tx", "fl", "mi", "dc"})
        void shouldAcceptValidStateCodesLowercase(String state) {
            assertTrue(EnumValidator.isValidEnum(state, UsaState.class));
        }

        @ParameterizedTest(name = "Should accept ''{0}'' as valid state code")
        @ValueSource(strings = {"Ca", "Ny", "Tx", "Fl", "Mi", "Dc", "\tCA\n"})
        void shouldAcceptValidStateCodesMixedCase(String state) {
            assertTrue(EnumValidator.isValidEnum(state, UsaState.class));
        }

        @Test
        void shouldAcceptStateWithWhitespace() {
            assertTrue(EnumValidator.isValidEnum("  CA  ", UsaState.class));
        }

        @Test
        void shouldRejectNull() {
            assertFalse(EnumValidator.isValidEnum(null, UsaState.class));
        }

        @Test
        void shouldRejectEmptyString() {
            assertFalse(EnumValidator.isValidEnum("", UsaState.class));
        }

        @Test
        void shouldRejectInvalidValue() {
            assertFalse(EnumValidator.isValidEnum("invalid", UsaState.class));
        }

        @Test
        void shouldRejectPartialValue() {
            assertFalse(EnumValidator.isValidEnum("M", UsaState.class));
        }
    }
}

