package com.example.second.dto;

import com.example.second.validation.constraint.UniqueTaskTitle;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class TaskRequest {
    
    @NotBlank(message = "Title must not be blank")
    @Size(max = 100, message = "Title must be at most 100 characters")
    @UniqueTaskTitle
    private String title;

    @Size(max = 500, message = "Description must be at most 500 characters")
    private String description;
    // Getters and Setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

}
