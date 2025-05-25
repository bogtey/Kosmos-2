package com.example.surveysystem.controller;

import com.example.surveysystem.dal.DataAccessLayer;
import com.example.surveysystem.models.Man;
import com.example.surveysystem.models.Photo;
import com.example.surveysystem.models.Survey;
import com.example.surveysystem.repositories.SurveyRepository;
import com.example.surveysystem.services.PhotoService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.Map;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@CrossOrigin(origins = "http://localhost:3333")
@RequestMapping("/admin")
public class AdminController {
    private final DataAccessLayer dataAccessLayer;
    private final PhotoService photoService;
    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    @Autowired
    public AdminController(DataAccessLayer dataAccessLayer, PhotoService photoService) {
        this.dataAccessLayer = dataAccessLayer;
        this.photoService = photoService;
    }


    @PostMapping("/create/survey/")
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> createSurvey(@RequestBody Survey survey) {
        dataAccessLayer.createSurvey(survey);
        return ResponseEntity.ok("Create!");
    }

    @PostMapping("/delete/survey/{id}")
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @CrossOrigin(origins = "http://localhost:3333")
    public ResponseEntity deleteSurveyById(@PathVariable("id") long id) {
        dataAccessLayer.deleteSurvey(id);
        return ResponseEntity.ok("Delete!");
    }

    @PutMapping("/update/survey/{id}")
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity updateSurveyById(@PathVariable("id")
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
    @Autowired
    private SurveyRepository surveyRepository;
    @GetMapping("/get/surveys/{id}")
    @Transactional // Добавьте эту аннотацию
    public ResponseEntity<Survey> getSurveyById(@PathVariable("id") long id) {
        try {
            // Используйте JOIN FETCH для загрузки опроса с фотографиями
            Survey survey = surveyRepository.findByIdWithPhotos(id)
                    .orElseThrow(() -> new EntityNotFoundException("Survey not found"));
            return ResponseEntity.ok(survey);
        } catch (Exception e) {
            logger.error("Error fetching survey with id {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/get/surveys")
    public ResponseEntity<List<Survey>> getSurveys() {
        try {
            List<Survey> surveys = dataAccessLayer.getSurveys();
            return ResponseEntity.ok(surveys);
        } catch (Exception e) {
            logger.error("Error fetching surveys: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/get/all/surveys/{manId}")
    public ResponseEntity<List<Survey>> getSurveysByManId(@PathVariable("manId") long manId) {
        return ResponseEntity.ok(dataAccessLayer.getSurveysByManId(manId));
    }

    @PostMapping("/upload/photo/{surveyId}/{manId}")
    public ResponseEntity<?> uploadPhoto(@PathVariable Long surveyId, @PathVariable Long manId, @RequestParam("file") MultipartFile file) {
        logger.info("Uploading photo for surveyId: {}, manId: {}", surveyId, manId);

        if (file.isEmpty()) {
            logger.warn("Uploaded file is empty");
            return ResponseEntity.badRequest().body("Error: Uploaded file is empty");
        }

        try {
            photoService.savePhoto(surveyId, manId, file);
            return ResponseEntity.ok("Photo uploaded successfully");
        } catch (IllegalArgumentException e) {
            logger.error("Error: {}", e.getMessage());
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Unexpected error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error: " + e.getMessage());
        }
    }

    @GetMapping("/get/photos/{manId}/{surveyId}")
    public ResponseEntity<List<Photo>> getPhotosByManIdAndSurveyId(@PathVariable Long manId, @PathVariable Long surveyId) {
        List<Photo> photos = photoService.getPhotosByManIdAndSurveyId(manId, surveyId);
        return ResponseEntity.ok(photos);
    }

    @GetMapping("/get/photo/{manId}/{surveyId}")
    public ResponseEntity<byte[]> getPhoto(@PathVariable Long manId, @PathVariable Long surveyId) {
        List<Photo> photos = photoService.getPhotosByManIdAndSurveyId(manId, surveyId);
        if (!photos.isEmpty()) {
            Photo photo = photos.get(0); // Получаем первую фотографию
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(photo.getContentType()))
                    .body(photo.getData());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/get/photo/{photoId}")
    public ResponseEntity<byte[]> getPhotoById(@PathVariable Long photoId) {
        logger.info("Fetching photo with photoId: {}", photoId);

        // Получаем фотографию по photoId
        Photo photo = photoService.getPhotoById(photoId);

        if (photo != null) {
            // Возвращаем фотографию с правильным типом контента
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(photo.getContentType()))
                    .body(photo.getData());
        } else {
            // Если фотография не найдена, возвращаем 404
            logger.warn("Photo not found for photoId: {}", photoId);
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/get/all/photos")
    public ResponseEntity<List<Photo>> getAllPhotos() {
        List<Photo> photos = photoService.getAllPhotos();
        if (photos.isEmpty()) {
            return ResponseEntity.noContent().build(); // Возвращаем 204 No Content, если нет фотографий
        }
        return ResponseEntity.ok(photos); // Возвращаем список фотографий
    }

    @DeleteMapping("/delete/photo/{surveyId}")
    public ResponseEntity<String> deletePhotoBySurveyId(@PathVariable Long surveyId) {
        try {
            // Получаем все фотографии, связанные с опросом
            List<Photo> photos = photoService.getPhotosBySurveyId(surveyId);

            // Удаляем каждую фотографию
            for (Photo photo : photos) {
                photoService.deletePhoto(photo.getId());
            }

            return ResponseEntity.ok("Photos deleted successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            // Обработка других возможных ошибок
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error: " + e.getMessage());
        }
    }

//    @GetMapping("/get/pseudonym/{id}")
//    public ResponseEntity<String> getPseudonymById(@PathVariable("id") long id) {
//        Man man = dataAccessLayer.getMan(id);
//        if (man != null) {
//            return ResponseEntity.ok(man.getPseudonym());
//        } else {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Пользователь не найден");
//        }
//    }
//
//    @PutMapping("/update/pseudonym/{id}")
//    public ResponseEntity<String> updatePseudonym(@PathVariable("id") long id, @RequestBody Map<String, String> body) {
//        String newPseudonym = body.get("pseudonym"); // Извлекаем псевдоним из объекта
//        Man man = dataAccessLayer.getMan(id);
//        if (man != null) {
//            man.setPseudonym(newPseudonym);
//            dataAccessLayer.updateMan(id, man);
//            return ResponseEntity.ok("Псевдоним обновлен");
//        } else {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Пользователь не найден");
//        }
//    }
}
