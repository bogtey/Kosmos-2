package com.example.surveysystem.dto;

import lombok.Data;
@Data
public class SignupRequest {
    private String name;
    private String surname;
    private String password;
    private String address;
    private String age;
}