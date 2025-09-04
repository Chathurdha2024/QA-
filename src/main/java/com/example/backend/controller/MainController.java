package com.example.backend.controller;

import java.security.Key;
import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.model.User;
import com.example.backend.repository.UserRepo;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@RestController
@RequestMapping("/api") // all endpoints will be under /api
@CrossOrigin(origins = "http://localhost:5173") // allow Vite frontend requests
public class MainController {

    @Autowired
    UserRepo userRepo;

    // Signup
@PostMapping("/addUser")
public ResponseEntity<?> addUser(@RequestBody User user) {

    // Validate required fields
    if (user.getUsername() == null || user.getUsername().trim().isEmpty() ||
        user.getEmail() == null || user.getEmail().trim().isEmpty() ||
        user.getPassword() == null || user.getPassword().trim().isEmpty()) {

        return ResponseEntity.badRequest().body(Map.of(
            "message", "Username, email and password are required"
        ));
    }

    // Check if username already exists
    if (userRepo.findByUsername(user.getUsername()) != null) {
        return ResponseEntity.badRequest().body(Map.of("message", "Username already exists"));
    }

    // Save the user
    User savedUser = userRepo.save(user);

    // Return id, username, and email in response (ordered)
        Map<String, Object> response = new java.util.LinkedHashMap<>();
        response.put("message", "User Registered Successfully");
        response.put("id", savedUser.getId());
        response.put("username", savedUser.getUsername());
        response.put("email", savedUser.getEmail());

        return ResponseEntity.status(201).body(response);
}


    // Login with username + password
@PostMapping("/login")
public ResponseEntity<?> login(@RequestBody User user) {

    User existingUser = userRepo.findByUsernameAndPassword(user.getUsername(), user.getPassword());
    if (existingUser != null) {
        // Generate JWT token
        String secretKey = "mySecretKey12345mySecretKey12345"; // at least 32 chars
        Key key = Keys.hmacShaKeyFor(secretKey.getBytes());

        String token = Jwts.builder()
                .setSubject(existingUser.getUsername())
                .claim("email", existingUser.getEmail())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 1 day expiry
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        // preserve insertion order
        Map<String, Object> response = new java.util.LinkedHashMap<>();
        response.put("message", "Login successful");
        response.put("token", token);

        return ResponseEntity.ok(response);
    } else {
        return ResponseEntity.status(400).body(Map.of("message", "Invalid username or password"));
    }
}
}
