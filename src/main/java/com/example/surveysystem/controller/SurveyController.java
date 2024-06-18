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

@Slf4j
@RestController
@CrossOrigin(origins = "http://localhost:3333")
@RequestMapping("/user")
public class SurveyController {
    private final DataAccessLayer dataAccessLayer;
    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);


    @Autowired
    public SurveyController(DataAccessLayer dataAccessLayer) {
        this.dataAccessLayer = dataAccessLayer;
    }



    @GetMapping("/get/survey/{id}")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity getSurveyById(@PathVariable("id") long id){
        return ResponseEntity.ok(dataAccessLayer.getSurvey(id));}
    @GetMapping("/get/surveys")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity getSurveys(){
        return ResponseEntity.ok(dataAccessLayer.getSurveys());}

    @GetMapping("/get/man/{id}")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity getManById(@PathVariable("id") long id){
        return ResponseEntity.ok(dataAccessLayer.getMan(id));}
    @GetMapping("/get/mans")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity getMans(){
        return ResponseEntity.ok(dataAccessLayer.getMans());}
    @PostMapping("/create/survey")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> createSurvey(@RequestBody Survey survey) {
        dataAccessLayer.createSurvey(survey);
        return ResponseEntity.ok("Create!");
    }


}

