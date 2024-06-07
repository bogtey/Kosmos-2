package com.example.surveysystem.controller;


import com.example.surveysystem.DemoApplication;
import com.example.surveysystem.dto.SigninRequest;
import com.example.surveysystem.dto.SignupRequest;
import com.example.surveysystem.security.JwtCore;
//import com.example.surveysystem.service.UserDetailsServiceImpl;
import com.example.surveysystem.service.UserDetailsServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@Slf4j
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:3333")
public class SecurityController {
    private final UserDetailsServiceImpl userService;
    @Autowired
    public SecurityController(UserDetailsServiceImpl userService) {
        this.userService = userService;
    }
    @Autowired
    private JwtCore jwtCore;

    @PostMapping("/signup")
    @CrossOrigin(origins = "http://localhost:3000")
    ResponseEntity<?> signup(@RequestBody SignupRequest signupRequest) {
        String serviceResult = userService.newUser(signupRequest);
        if (Objects.equals(serviceResult, "Выберите другое имя")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(serviceResult);
        }
        if (Objects.equals(serviceResult, "Выберите другую почту")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(serviceResult);
        }
        return ResponseEntity.ok("Вы успешно зарегистрированы. Теперь можете войти в свой аккаунт.");
    }
    @PostMapping("/signin")
    @CrossOrigin(origins = "http://localhost:3000")
    ResponseEntity<?> signin(@RequestBody SigninRequest signinRequest) {

        UserDetails user = userService.loadUserByUsername(signinRequest.getName());

        if (Objects.equals(user, null) || !Objects.equals(user.getPassword(), signinRequest.getSurname())) {
            log.info("Ошибка авторизации пользователя " + signinRequest.getName());
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        String jwt = jwtCore.generateToken(user);

        DemoApplication.currentUser = userService.loadUserEntityByUsername(signinRequest.getName());
        log.info("Вход прошёл успешно");
        return ResponseEntity.ok(jwt);
    }
}
