package com.example.surveysystem.models;

import jakarta.persistence.*;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Data
@Table(name = "photo", schema = "schema")
public class Photo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Это будет photo_id

    @ManyToOne
    @JoinColumn(name = "man_id") // Убедитесь, что это имя соответствует вашей таблице
    private Man man;

    @ManyToOne
    @JoinColumn(name = "surveyId", referencedColumnName = "surveyId")
    @JsonBackReference // Добавлено для предотвращения бесконечной рекурсии
    private Survey survey;

    @Lob
    private byte[] data; // Данные изображения

    private String contentType; // Тип содержимого (например, image/jpeg)
    private String fileName; // Имя файла
    @Lob

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
    public Long getSurveyId() {
        return survey != null ? survey.getSurveyId() : null; // Возвращаем surveyId, если survey не null
    }

    // Другие поля и методы, если необходимо
}