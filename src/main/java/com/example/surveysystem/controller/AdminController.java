package com.example.surveysystem.controller;

import com.example.surveysystem.dal.DataAccessLayer;
import com.example.surveysystem.models.Man;
import com.example.surveysystem.models.Survey;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@CrossOrigin(origins = "http://localhost:3001")
@RequestMapping("/admin")
public class AdminController {
    private final DataAccessLayer dataAccessLayer;
    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    @Autowired
    public AdminController(DataAccessLayer dataAccessLayer) {
        this.dataAccessLayer = dataAccessLayer;
    }

    @PostMapping("/create/survey/")
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> createSurvey(@RequestBody Survey survey) {
        dataAccessLayer.createSurvey(survey);
        return ResponseEntity.ok("Create!");
    }

    @PostMapping("/delete/survey/{id}")
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @CrossOrigin(origins = "http://localhost:3001")
    public ResponseEntity deleteSurveyById(@PathVariable("id") long id) {
        dataAccessLayer.deleteSurvey(id);
        return ResponseEntity.ok("Delete!");
    }

    @PostMapping("/update/survey/{id}")
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity updateSurveylById(@PathVariable("id")
                                            long id, @RequestBody Survey newSurvey) {
        dataAccessLayer.updateSurvey(id, newSurvey);
        return ResponseEntity.ok("Update!");
    }



    @PostMapping("/create/man/")
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> createMan(@RequestBody Man man) {
        dataAccessLayer.createMan(man);
        return ResponseEntity.ok("Create!");
    }

    @DeleteMapping("/delete/man/{id}")
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity deleteManById(@PathVariable("id") long id) {
        dataAccessLayer.deleteMan(id);
        return ResponseEntity.ok("Delete!");
    }

    @PostMapping("/update/man/{id}")
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity updateManlById(@PathVariable("id")
                                         long id, @RequestBody Man newMan) {
        dataAccessLayer.updateMan(id, newMan);
        return ResponseEntity.ok("Update!");
    }
    @GetMapping("/get/surveys/{id}")
//    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<Survey> getSurveyById(@PathVariable("id") long id) {
        Survey survey = dataAccessLayer.getSurvey(id);
        return ResponseEntity.ok(survey);
    }

    @GetMapping("/get/surveys")
//    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<List<Survey>> getSurveys() {
        List<Survey> surveys = dataAccessLayer.getSurveys();
        return ResponseEntity.ok(surveys);
    }
    @GetMapping("/get/all/surveys/{manId}")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<Survey>> getSurveysByManId(@PathVariable("manId") long manId) {
        return ResponseEntity.ok(dataAccessLayer.getSurveysByManId(manId));
    }
}
