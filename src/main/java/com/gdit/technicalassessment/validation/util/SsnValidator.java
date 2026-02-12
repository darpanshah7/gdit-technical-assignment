package com.gdit.technicalassessment.validation.util;

public class SsnValidator {

    private static final String SSN_PATTERN = "\\d{9}";

    private SsnValidator() {
        throw new IllegalStateException("Utility class");
    }

    public static boolean isValidSsn(String ssn) {
        if (ssn == null || ssn.isEmpty()) {
            return false;
        }

        String cleanedSsn = cleanSsn(ssn);
        return cleanedSsn.matches(SSN_PATTERN);
    }

    public static String cleanSsn(String ssn) {
        //return ssn.trim().replaceAll("[\\s-]", "");
        return ssn.replaceAll("[\\s-]", "");
    }
}

