package com.example.second.model;

import com.example.second.validation.constraint.PasswordNoUsername;
import com.example.second.validation.constraint.ValidName;
import com.example.second.validation.constraint.ValidPassword;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

@PasswordNoUsername
public class User {
    private String id;

    @NotBlank
    @ValidName(message = "Username must contain only letters and spaces")
    private String username;

    @NotBlank
    @Email
    @NotEmpty
    private String email;

    @ValidPassword(min = 8, requireUppercase = true, requireLowercase = true, requireDigit = true, requireSpecial = true)
    private String password;
    private String role;

    
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
}
