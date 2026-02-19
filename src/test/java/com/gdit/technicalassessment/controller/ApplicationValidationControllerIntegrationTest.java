package com.gdit.technicalassessment.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gdit.technicalassessment.model.Application;
import com.gdit.technicalassessment.model.Household;
import com.gdit.technicalassessment.model.Income;
import com.gdit.technicalassessment.model.StudentInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


import java.time.LocalDate;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class ApplicationValidationControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;


//    @MockitoBean
//    private ApplicationValidationService validationService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void validApplicationReturns200WithValidStatus() throws Exception {

        Application application = Application.builder()
                .studentInfo(StudentInfo.builder()
                        .firstName("Jane")
                        .lastName("Smith")
                        .ssn("123456789")
                        .dateOfBirth(LocalDate.of(2003,05,15))
                        .build())
                .dependencyStatus("dependent")
                .maritalStatus("single")
                .household(new Household(4,1))
                .income(new Income(5000,65000))
                .stateOfResidence("CA")
                .build();


        mockMvc.perform(post("/api/v1/applications/validate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(application)))
               // .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("VALID"))
                .andExpect(jsonPath("$.failedRules").isEmpty());
    }
}