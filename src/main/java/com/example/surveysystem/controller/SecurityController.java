package com.example.surveysystem.controller;

import com.example.surveysystem.dal.DataAccessLayer;
import com.example.surveysystem.dto.AuthResponse;
import com.example.surveysystem.dto.SigninRequest;
import com.example.surveysystem.dto.SignupRequest;
import com.example.surveysystem.models.Man;
import com.example.surveysystem.models.Role;
import com.example.surveysystem.security.JwtCore;
import com.example.surveysystem.service.UserDetailsServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@Slf4j
@CrossOrigin(origins = "http://localhost:3333")
@RequestMapping("/auth")
public class SecurityController {

    private final UserDetailsServiceImpl userService;
    private final DataAccessLayer dataAccessLayer;
    private static final Logger errorLogger = LoggerFactory.getLogger("ERROR_LOGGER");
    private static final Logger logger = LoggerFactory.getLogger(SecurityController.class);

    @Autowired
    private JwtCore jwtCore;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    public SecurityController(UserDetailsServiceImpl userService, DataAccessLayer dataAccessLayer) {
        this.userService = userService;
        this.dataAccessLayer = dataAccessLayer;
    }

    @PostMapping("/signup")
    @CrossOrigin(origins = "http://localhost:3333")
    public ResponseEntity<?> signup(@RequestBody SignupRequest signupRequest) {
        // Убедитесь, что поле surname не null
        if (signupRequest.getSurname() == null || signupRequest.getSurname().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Фамилия не может быть пустой.");
        }

        // Шифрование пароля
        String encodedPassword = passwordEncoder.encode(signupRequest.getSurname()); // Используйте surname как пароль
        signupRequest.setSurname(encodedPassword); // Убедитесь, что вы устанавливаете зашифрованный пароль

        // Создаем нового пользователя
        Man newMan = new Man();
        newMan.setName(signupRequest.getName());
        newMan.setSurname(encodedPassword); // Установите зашифрованный пароль
        newMan.setPseudonym(signupRequest.getPseudonym());

        // Назначаем роль "user" по умолчанию
        Role userRole = dataAccessLayer.findRoleByName("ROLE_USER");
        if (userRole == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Роль ROLE_USER не найдена.");
        }
        newMan.getRoles().add(userRole);

        // Сохранение нового пользователя в базе данных
        String serviceResult = dataAccessLayer.newUserToDatabase(newMan);
        if (Objects.equals(serviceResult, "Выберите другое имя")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(serviceResult);
        }
        return ResponseEntity.ok("Вы успешно зарегистрированы. Теперь можете войти в свой аккаунт.");
    }



    @PostMapping("/signin")
    @CrossOrigin(origins = "http://localhost:3333")
    public ResponseEntity<?> signin(@RequestBody SigninRequest loginRequest) {
        // Получаем пользователя по имени
        Man user = dataAccessLayer.getUserFromDatabaseByUsername(loginRequest.getName());
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Пользователь не найден.");
        }


        // Проверяем пароль
        if (!passwordEncoder.matches(loginRequest.getSurname(), user.getSurname())) { // Используйте surname как пароль
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Неверный пароль.");
        }

        // Генерация токена
        UserDetails userDetails = userService.loadUserByUsername(loginRequest.getName());
        String token = jwtCore.generateToken(userDetails); // Используйте метод generateToken

        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        // Отправка токена клиенту
        return ResponseEntity.ok(new AuthResponse(token, roles)); // AuthResponse - класс, который содержит токен
    }



    @GetMapping("/check-pseudonym/{pseudonym}")
    public ResponseEntity<?> checkPseudonym(@PathVariable String pseudonym) {
        try {
            boolean exists = dataAccessLayer.checkPseudonymExists(pseudonym);
            return ResponseEntity.ok().body(exists);
        } catch (Exception e) {
            logger.error("Error checking pseudonym: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ошибка сервера. Попробуйте позже.");
        }
    }
}
