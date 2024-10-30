package com.example.surveysystem.models;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name = "survey", schema = "schema", catalog = "survey")
public class Survey {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long surveyId;

    @ManyToOne
    @JoinColumn(name = "manId")
    private Man man;

    @Column(name = "question")
    private String question;

    @Column(name = "title")
    private String title;

    @Column(name = "note")
    private String note;

    @Column(name = "vote")
    private int vote;


    @OneToMany(mappedBy = "survey", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference // Добавлено для управления сериализацией
    private List<Photo> photos;
}