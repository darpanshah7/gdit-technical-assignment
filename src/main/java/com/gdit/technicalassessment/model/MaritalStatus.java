package com.gdit.technicalassessment.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum MaritalStatus {
    SINGLE("single"),
    MARRIED("married");

    private final String value;

    MaritalStatus(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static MaritalStatus fromString(String value) {
        if (value == null) {
            return null;
        }
        for (MaritalStatus status : MaritalStatus.values()) {
            if (status.value.equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid marital status: " + value +
                ". Valid values are: single, married");
    }
}
