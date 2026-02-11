package com.gdit.technicalassessment.validation;

import com.gdit.technicalassessment.model.Application;
import com.gdit.technicalassessment.model.ValidationDetails;

public interface ValidationRule {

    String getRuleName();

    boolean isActive();

    ValidationDetails validate(Application application);
}
