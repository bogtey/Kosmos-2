package com.example.surveysystem.controller;

import com.example.surveysystem.dal.DataAccessLayer;
import com.example.surveysystem.models.Man;
import com.example.surveysystem.models.Survey;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@CrossOrigin(origins = "http://localhost:3333")
@RequestMapping("/unauthorized")
public class SurveyController {
    private final DataAccessLayer dataAccessLayer;


    @Autowired
    public SurveyController(DataAccessLayer dataAccessLayer) {
        this.dataAccessLayer = dataAccessLayer;
    }



    @GetMapping("/get/survey/{id}")public ResponseEntity getSurveyById(@PathVariable("id") long id){
        return ResponseEntity.ok(dataAccessLayer.getSurvey(id));}
    @GetMapping("/get/surveys")public ResponseEntity getSurveys(){
        return ResponseEntity.ok(dataAccessLayer.getSurveys());}

    @GetMapping("/get/man/{id}")public ResponseEntity getManById(@PathVariable("id") long id){
        return ResponseEntity.ok(dataAccessLayer.getMan(id));}
    @GetMapping("/get/mans")public ResponseEntity getMans(){
        return ResponseEntity.ok(dataAccessLayer.getMans());}

}

