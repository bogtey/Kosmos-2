package com.example.surveysystem.services;

import com.example.surveysystem.models.Man;
import com.example.surveysystem.models.Photo;
import com.example.surveysystem.models.Survey;
import com.example.surveysystem.repositories.ManRepository;
import com.example.surveysystem.repositories.PhotoRepository;
import com.example.surveysystem.repositories.SurveyRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

@Service
public class PhotoService {

    private static final Logger logger = LoggerFactory.getLogger(PhotoService.class);

    private final PhotoRepository photoRepository;
    private final SurveyRepository surveyRepository;
    private final ManRepository manRepository;

    @Autowired
    public PhotoService(PhotoRepository photoRepository, SurveyRepository surveyRepository, ManRepository manRepository) {
        this.photoRepository = photoRepository;
        this.surveyRepository = surveyRepository;
        this.manRepository = manRepository;
    }

    @Transactional
    public void savePhoto(Long surveyId, Long manId, MultipartFile file) {
        try {
            Survey survey = findSurveyById(surveyId);
            Man man = findManById(manId);

            Photo photo = new Photo();
            photo.setMan(man);
            photo.setContentType(file.getContentType());
            photo.setFileName(file.getOriginalFilename());
            photo.setSurvey(survey);

            try (InputStream inputStream = file.getInputStream()) {
                byte[] data = inputStream.readAllBytes();
                photo.setData(data);
            }

            photoRepository.save(photo);
        } catch (IOException e) {
            logger.error("Error saving photo: {}", e.getMessage());
            throw new PhotoServiceException("Error saving photo", e);
        } catch (Exception e) {
            logger.error("Unexpected error: {}", e.getMessage());
            throw new PhotoServiceException("Unexpected error occurred", e);
        }
    }

    @Transactional
    public List<Photo> getPhotosByMan(Long manId) {
        Man man = findManById (manId);
        return photoRepository.findByMan(man);
    }

    @Transactional
    public List<Photo> getPhotosByManIdAndSurveyId(Long manId, Long surveyId) {
        Man man = findManById(manId);
        Survey survey = findSurveyById(surveyId);
        return photoRepository.findByManAndSurvey(man, survey);
    }

    @Transactional
    public Photo getPhoto(Long photoId) {
        Optional<Photo> optionalPhoto = photoRepository.findById(photoId);
        return optionalPhoto.orElse(null);
    }

    @Transactional
    public List<Photo> getPhotosBySurveyId(Long surveyId) {
        Survey survey = findSurveyById(surveyId);
        return photoRepository.findBySurvey(survey);
    }

    @Transactional
    public void deletePhoto(Long photoId) {
        if (!photoRepository.existsById(photoId)) {
            throw new IllegalArgumentException("Photo not found with ID: " + photoId);
        }
        photoRepository.deleteById(photoId);
    }

    @Transactional
    public List<Photo> getAllPhotos() {
        return photoRepository.findAll();
    }

    @Transactional
    public Photo getPhotoById(Long photoId) {
        Optional<Photo> optionalPhoto = photoRepository.findById(photoId);
        return optionalPhoto.orElse(null);
    }

    private Man findManById(Long manId) {
        return manRepository.findById(manId)
                .orElseThrow(() -> new IllegalArgumentException("Man not found with ID: " + manId));
    }

    private Survey findSurveyById(Long surveyId) {
        return surveyRepository.findById(surveyId)
                .orElseThrow(() -> new IllegalArgumentException("Survey not found with ID: " + surveyId));
    }
}