package com.example.surveysystem.repositories;

import com.example.surveysystem.models.Man;
import com.example.surveysystem.models.Photo;
import com.example.surveysystem.models.Survey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, Long> {
    List<Photo> findByMan(Man man);
    List<Photo> findBySurvey(Survey survey);
    List<Photo> findByManAndSurvey(Man man, Survey survey);
}