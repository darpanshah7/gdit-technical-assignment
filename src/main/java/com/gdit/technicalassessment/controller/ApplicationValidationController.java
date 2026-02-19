package com.gdit.technicalassessment.controller;

import com.gdit.technicalassessment.model.Application;
import com.gdit.technicalassessment.model.ValidationDetails;
import com.gdit.technicalassessment.service.ApplicationValidationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/applications")
@RequiredArgsConstructor
public class ApplicationValidationController {

    private final ApplicationValidationService validationService;

    @PostMapping("/validate")
    public ResponseEntity<ValidationDetails> validateApplication(@RequestBody Application application) {
        log.info("Received validation request for application: {}", application);

        ValidationDetails validationDetails = validationService.validateApplication(application);
        return ResponseEntity.ok(validationDetails);
    }
}