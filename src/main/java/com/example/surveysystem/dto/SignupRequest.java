package com.example.surveysystem.dto;

import lombok.Data;

import java.util.Set;

@Data
public class SignupRequest {
    private String name;
    private Set<String> roles;
    private String surname;
    private String password;
    private String address;
    private String age;
}