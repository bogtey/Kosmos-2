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
@RequestMapping("/unauthorized")
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

    @PostMapping("/auth/signup")
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

    @PostMapping("/auth/signin")
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
    @GetMapping("/auth/get/surveys/{manId}")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<Survey>> getSurveysByManId(@PathVariable("manId") long manId) {
        return ResponseEntity.ok(dataAccessLayer.getSurveysByManId(manId));
    }
}
//    }
//    @DeleteMapping("/delete/baskets/{userId}")
//    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
//    public ResponseEntity<String> deleteBasketsByUserId(@PathVariable("userId") long userId) {
//        dataAccessLayer.deleteBasketsByUserId(userId);
//        return ResponseEntity.ok("baskets");
//    }
//    @PostMapping("/create/review")
//    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
//    public ResponseEntity<String> createReview(@RequestBody Review review) {
//        dataAccessLayer.createReview(review);
//        return ResponseEntity.ok("Review added successfully!");
//    }
//    @DeleteMapping("/delete/review/{id}")
//    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
//    public ResponseEntity<String> deleteReviewById(@PathVariable("id") long id) {
//        dataAccessLayer.deleteReviewById(id);
//        return ResponseEntity.ok("Review deleted successfully!");
//    }
//    @PostMapping("/create/order/{userId}")
//    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
//    public ResponseEntity<Order> createOrder(@PathVariable("userId") long userId) {
//        Order order = dataAccessLayer.createOrderWithBasketItems(userId);
//        return ResponseEntity.ok(order);
//    }
//    @DeleteMapping("/delete/order/{id}")
//    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
//    public ResponseEntity<String> deleteOrderById(@PathVariable("id") long id) {
//        dataAccessLayer.deleteOrderById(id);
//        return ResponseEntity.ok("Order deleted successfully!");
//    }
//    @PostMapping("/create/basket")
//    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
//    public ResponseEntity<String> createUser(@RequestBody Basket basket) {
//        dataAccessLayer.createBasket(basket);
//        return ResponseEntity.ok("Basket added successfully!");
//    }
//
//    @GetMapping("/get/user/{id}")
//    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
//    public ResponseEntity<User> getUserById(@PathVariable("id") long id) {
//        return ResponseEntity.ok(dataAccessLayer.getUserById(id));
//    }
//
//    @GetMapping("/get/baskets/{userId}")
//    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
//    public ResponseEntity<List<Basket>> getBasketsByUserId(@PathVariable("userId") long userId) {
//        return ResponseEntity.ok(dataAccessLayer.getBasketsByUserId(userId));
//    }
//
//    @PutMapping("/update/user/{id}")
//    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
//    public ResponseEntity<String> updateUserById(@PathVariable("id") long id, @RequestBody User updatedUser) {
//        dataAccessLayer.updateUser(id, updatedUser);
//        return ResponseEntity.ok("User updated successfully!");
//    }
//
//    @GetMapping("/get/order/{orderId}")
//    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
//    public ResponseEntity<Order> getOrderById(@PathVariable("orderId") Long orderId) {
//        Order order = dataAccessLayer.getOrderById(orderId);
//        return ResponseEntity.ok(order);
//    }

