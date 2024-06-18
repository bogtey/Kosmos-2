package com.example.surveysystem.controller;

import com.example.surveysystem.DemoApplication;
import com.example.surveysystem.dal.DataAccessLayer;
import com.example.surveysystem.dto.SigninRequest;
import com.example.surveysystem.dto.SignupRequest;

import com.example.surveysystem.exception.UnauthorizedException;
import com.example.surveysystem.models.*;
import com.example.surveysystem.security.JwtCore;
import com.example.surveysystem.service.UserDetailsServiceImpl;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;
import java.util.Set;

@RestController
@Slf4j
@CrossOrigin(origins = "http://localhost:3333")
@RequestMapping("/auth")
public class SecurityController {

    private final UserDetailsServiceImpl userService;

    private final DataAccessLayer dataAccessLayer;
    private static final Logger errorLogger = LoggerFactory.getLogger("ERROR_LOGGER");
    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);


    @Autowired
    public SecurityController(UserDetailsServiceImpl userService, DataAccessLayer dataAccessLayer) {
        this.userService = userService;
        this.dataAccessLayer = dataAccessLayer;
    }

    @Autowired
    private JwtCore jwtCore;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/signup")
    @CrossOrigin(origins = "http://localhost:3333")
    public ResponseEntity<?> signup(@RequestBody SignupRequest signupRequest) {
        signupRequest.setSurname(passwordEncoder.encode(signupRequest.getSurname()));
        signupRequest.setRoles(Set.of("ROLE_USER"));
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
    @CrossOrigin(origins = "http://localhost:3333")
    public ResponseEntity<?> signin(@RequestBody SigninRequest signinRequest) {
        UserDetails user = userService.loadUserByUsername(signinRequest.getName());
        if (user == null || !passwordEncoder.matches(signinRequest.getSurname(), user.getPassword())) {
            logger.info("Ошибка авторизации пользователя " + signinRequest.getName());
            throw new UnauthorizedException("Ошибка авторизации пользователя " + signinRequest.getName());
        }
        String jwt = jwtCore.generateToken(user);
        DemoApplication.currentUser = userService.loadUserEntityByUsername(signinRequest.getName());
        logger.info("Вход прошёл успешно");
        return ResponseEntity.ok(jwt);
    }
}