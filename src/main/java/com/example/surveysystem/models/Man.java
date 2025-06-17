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

    @Column(name = "pseudonym") // Новое поле
    private String pseudonym;

    @CollectionTable(name = "User _Role", schema = "schema", joinColumns = @JoinColumn(name = "manId"))
    @Column(name = "role")
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "man_roles",
            joinColumns = @JoinColumn(name = "man_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();
}