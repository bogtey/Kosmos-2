package com.example.surveysystem.models;

import jakarta.persistence.*;
import jdk.jfr.DataAmount;
import lombok.Data;
import org.hibernate.annotations.Type;

@Entity
@Data
@Table(name = "survey", schema = "schema", catalog = "survey")
public class Survey {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
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
}