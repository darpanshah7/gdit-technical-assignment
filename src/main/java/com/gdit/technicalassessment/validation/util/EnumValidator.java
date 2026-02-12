package com.gdit.technicalassessment.validation.util;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EnumValidator {

    private EnumValidator() {
        throw new IllegalStateException("Utility class");
    }

    public static <T extends Enum<T>> boolean isValidEnum(String stringValue, Class<T> enumClass) {
        if (stringValue == null) {
            log.warn("Provided value is null for enum validation against {}", enumClass.getSimpleName());
            return false;
        }

        try {
            Enum.valueOf(enumClass, stringValue.toUpperCase().trim());
            return true;
        } catch (IllegalArgumentException e) {
            log.warn("Provided value {} is not a valid enum value for {}", stringValue, enumClass.getSimpleName());
            return false;
        }
    }

    public static <T extends Enum<T>> T parseEnum(String stringValue, Class<T> enumClass) {
        if (stringValue == null) {
            log.warn("Provided value is null for enum validation against {}", enumClass.getSimpleName());
            throw new IllegalArgumentException("Provided value is null for enum parse");
        }
        return Enum.valueOf(enumClass, stringValue.toUpperCase().trim());
    }
}

