package com.example.surveysystem.repositories;

import com.example.surveysystem.models.Survey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SurveyRepository extends JpaRepository<Survey, Long> {
    @Query("SELECT s FROM Survey s LEFT JOIN FETCH s.photos WHERE s.surveyId = :id")
    Optional<Survey> findByIdWithPhotos(@Param("id") Long id);
}