package com.example.second.validation.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

import com.example.second.model.User;
import com.example.second.repository.TaskRepository;
import com.example.second.validation.constraint.UniqueTaskTitle;

import jakarta.validation.ConstraintValidator;

public class UniqueTaskTitleValidator implements ConstraintValidator<UniqueTaskTitle, String> {

    @Autowired
    TaskRepository taskRepository;

    private String getUserId(){
        return ((User)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
    }


    @Override
    public boolean isValid(String value, jakarta.validation.ConstraintValidatorContext context) {
        String currentUserId = getUserId();
        if (value == null || value.isBlank()) {
            return true; // Let other annotations handle blank/null
        }
        // Check for existence of another task with this title for this user
        return !taskRepository.existsByTitleAndOwnerId(value, currentUserId);
    }
    
}
