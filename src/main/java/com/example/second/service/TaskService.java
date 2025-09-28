package com.example.second.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.second.dto.TaskDetails;
import com.example.second.model.Task;
import com.example.second.repository.TaskRepository;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    // Create task for a user
    public int createTask(Task task) {
        return taskRepository.save(task);
    }

    // Find task by ID (for owner or admin)
    public TaskDetails getTaskById(String id) {
        return taskRepository.findById(id);
    }

    // List tasks for owner
    public List<Task> getTasksByOwner(String ownerId) {
        return taskRepository.findByOwner(ownerId);
    }

    // List all tasks (admin)
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    // Update a task (check ownership in controller/service)
    public int updateTask(Task task) {
        return taskRepository.update(task);
    }

    // Delete task
    public int deleteTask(String id) {
        return taskRepository.deleteById(id);
    }
}
