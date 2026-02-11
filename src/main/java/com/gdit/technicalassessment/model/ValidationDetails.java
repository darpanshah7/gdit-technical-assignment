package com.gdit.technicalassessment.model;

import java.util.List;

public record ValidationDetails(
        ValidationStatus status,
        List<String> errors
) {

}
