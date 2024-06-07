package com.example.surveysystem.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "man", schema = "schema", catalog = "survey")

public class Man {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long manId;
    @Column(name = "name")
    private String name;
    @Column(name = "surname")
    private String surname;
    @Column(name = "age")
    private int age;
    @Column(name = "password")
    private String password;
    @Column(name = "address")
    private String address;
    @Column(name = "email")
    private String email;
}
