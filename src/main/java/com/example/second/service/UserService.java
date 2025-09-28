package com.example.second.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.second.model.User;
import com.example.second.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

    // Register new user (with password encryption)
    public int registerUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    // List all users (admin only)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Get user by username
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    // Get user by ID
    public User getUserById(String id) {
        return userRepository.findById(id);
    }

    // Change user role/password
    public int updateUser(String id, User incoming) {
        // 1. Get existing user from DB
        User existing = getUserById(id);
        if (existing == null) throw new RuntimeException("User not found");

        // 2. Update fields only if not null (partial update/patch style)
        if (incoming.getEmail() != null && !incoming.getEmail().isEmpty()) {
            existing.setEmail(incoming.getEmail());
        }
        if (incoming.getUsername() != null && !incoming.getUsername().isEmpty()) {
            existing.setUsername(incoming.getUsername());
        }
        if (incoming.getRole() != null && !incoming.getRole().isEmpty()) {
            existing.setRole(incoming.getRole());
        }
        if (incoming.getPassword() != null && !incoming.getPassword().isEmpty()) {
            existing.setPassword(passwordEncoder.encode(incoming.getPassword()));
        }

        // 3. Call repository to persist changes
        return userRepository.update(id, existing);
    }

    // Delete user
    public int deleteUser(String id) {
        return userRepository.deleteById(id);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public boolean existsByEmail(String email) {
        try {
            User user = userRepository.findByEmail(email);
            return user != null;
        } catch (Exception e) {
            return false;
        }
    }
    

    
}
