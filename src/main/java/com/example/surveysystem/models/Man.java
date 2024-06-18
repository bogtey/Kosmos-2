package com.example.surveysystem.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

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

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "User_Role", schema = "schema", joinColumns = @JoinColumn(name = "manId"))
    @Column(name = "role")
    private Set<String> roles = new HashSet<>();
}
