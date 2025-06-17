package com.example.surveysystem.models;
import jakarta.persistence.*;
import lombok.Data;
@Entity
@Data
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name; // Например, "ROLE_ADMIN" или "ROLE_USER"
}
