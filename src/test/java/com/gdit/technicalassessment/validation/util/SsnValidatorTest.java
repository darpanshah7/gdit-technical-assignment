package com.gdit.technicalassessment.validation.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SsnValidatorTest {

    @Test
    void ssnIsValid() {
        assertTrue(SsnValidator.isValidSsn("123456789"));
    }

    @Test
    void ssnIsValidWithDashes() {
        assertTrue(SsnValidator.isValidSsn("123-45-6789"));
    }

    @Test
    void ssnIsValidWithSpaces() {
        assertTrue(SsnValidator.isValidSsn("123 45 6789"));
    }

    @Test
    void ssnIsValidWithMixedFormatting() {
        assertTrue(SsnValidator.isValidSsn("123-45 6789"));
    }

    @Test
    void ssnIsValidWithLeadingAndTrailingSpaces() {
        assertTrue(SsnValidator.isValidSsn("  123456789  "));
    }

    @Test
    void ssnIsInvalidWhenNull() {
        assertFalse(SsnValidator.isValidSsn(null));
    }

    @Test
    void ssnIsInvalidWhenEmpty() {
        assertFalse(SsnValidator.isValidSsn(""));
    }

    @Test
    void ssnIsInvalidWhenTooShort() {
        assertFalse(SsnValidator.isValidSsn("12345678"));
    }

    @Test
    void ssnIsInvalidWhenTooLong() {
        assertFalse(SsnValidator.isValidSsn("1234567890"));
    }

    @Test
    void ssnIsInvalidWithLetters() {
        assertFalse(SsnValidator.isValidSsn("12345678A"));
    }

    @Test
    void ssnIsInvalidWithSpecialCharacters() {
        assertFalse(SsnValidator.isValidSsn("123@45#6789"));
    }

    @Test
    void ssnIsInvalidWhenAllLetters() {
        assertFalse(SsnValidator.isValidSsn("ABCDEFGHI"));
    }

    @Test
    void ssnIsInvalidWithOnlyDashes() {
        assertFalse(SsnValidator.isValidSsn("---"));
    }

    @Test
    void ssnIsInvalidWithOnlySpaces() {
        assertFalse(SsnValidator.isValidSsn("   "));
    }

    @Test
    void cleanSsnRemovesDashes() {
        assertEquals("123456789", SsnValidator.cleanSsn("123-45-6789"));
    }

    @Test
    void cleanSsnRemovesSpaces() {
        assertEquals("123456789", SsnValidator.cleanSsn("123 45 6789"));
    }

    @Test
    void cleanSsnRemovesDashesAndSpaces() {
        assertEquals("123456789", SsnValidator.cleanSsn("123-45 6789"));
    }

    @Test
    void cleanSsnHandlesAlreadyCleanSsn() {
        assertEquals("123456789", SsnValidator.cleanSsn("123456789"));
    }

    @Test
    void cleanSsnRemovesMultipleSpaces() {
        assertEquals("123456789", SsnValidator.cleanSsn("123  456  789"));
    }

    @Test
    void cleanSsnRemovesLeadingAndTrailingWhitespace() {
        assertEquals("123456789", SsnValidator.cleanSsn("  123-45-6789  "));
    }
}

