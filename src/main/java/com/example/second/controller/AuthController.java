package com.example.second.controller;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.second.dto.LoginRequest;
import com.example.second.model.User;
import com.example.second.security.JwtService;
import com.example.second.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Registration endpoint (example; you can also put under /api/users)
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody User user) {
        // Only save if user doesn't exist
        if (userService.existsByEmail(user.getEmail())) {
            return ResponseEntity.badRequest().body(Map.of("error", "Email already registered"));
        }
        userService.registerUser(user); // Use your own save logic
        return ResponseEntity.ok(Map.of("message", "Registration successful"));
    }

    // Login endpoint: returns JWT if valid
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest authRequest) {
        User user = userService.findByEmail(authRequest.getEmail());
        if (user == null) {
            return ResponseEntity.status(401).body(Map.of("error", "Email not registered"));
        }
        if (!passwordEncoder.matches(authRequest.getPassword(), user.getPassword())) {
            // Handle possible plaintext-stored password migration:
            // If the stored password exactly equals the raw password, encode & update it, then continue.
            if (authRequest.getPassword().equals(user.getPassword())) {
                user.setPassword(passwordEncoder.encode(user.getPassword()));
                userService.updateUser(user.getId(),user); // persist migrated password
            } else {
                System.out.println(user.getEmail());
                return ResponseEntity.status(401).body(Map.of("error", "Invalid credentials"));
            }
        }
        // Prepare claims (role and email are optional extras)
        Map<String, Object> claims = Map.of(
            "role", user.getRole(),
            "email", user.getEmail()
        );
        // Issue JWT with userId as subject (sub)
        String token = jwtService.generateToken(user.getId(), claims);
        return ResponseEntity.ok(Map.of(
            "token", token,
            "userId", user.getId(),
            "role", user.getRole(),
            "email", user.getEmail()
        ));
    }

    // DTO for login
    public static class AuthRequest {
        private String email;
        private String password;
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }
}
