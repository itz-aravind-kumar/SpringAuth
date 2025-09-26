package com.example.second.controller;
import com.example.second.model.Task;
import com.example.second.service.TaskService;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    // Create task
    @PostMapping("/")
    public ResponseEntity<?> create(@RequestBody Task task) {
        taskService.createTask(task);
        return ResponseEntity.ok("Task created");
    }

    // List tasks for owner
    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<List<Task>> getUserTasks(@PathVariable String ownerId) {
        return ResponseEntity.ok(taskService.getTasksByOwner(ownerId));
    }

    // List all tasks (admin only)
    @GetMapping("/")
    public ResponseEntity<List<Task>> getAllTasks() {
        return ResponseEntity.ok(taskService.getAllTasks());
    }

    // Get single task
    @GetMapping("/{id}")
    public ResponseEntity<?> getTask(@PathVariable String id) {
        return ResponseEntity.ok(taskService.getTaskById(id));
    }

    // Update
    @PutMapping("/{id}")
    public ResponseEntity<?> updateTask(@PathVariable String id, @RequestBody Task task) {
        task.setId(id);
        taskService.updateTask(task);
        return ResponseEntity.ok("Task updated");
    }

    // Delete
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTask(@PathVariable String id) {
        taskService.deleteTask(id);
        return ResponseEntity.ok("Task deleted");
    }
}
