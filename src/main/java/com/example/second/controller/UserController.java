package com.example.second.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.second.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    // List all users (admin only, will restrict with JWT/roles later)
    @GetMapping("/")
    public ResponseEntity<?> allUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    // Get user by username
    @GetMapping("/{id}")
    public ResponseEntity<?> user(@PathVariable String id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }
}
