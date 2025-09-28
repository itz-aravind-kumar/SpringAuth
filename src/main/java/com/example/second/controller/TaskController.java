package com.example.second.controller;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.security.core.context.SecurityContextHolder;

import com.example.second.dto.TaskDetails;
import com.example.second.model.User;
import com.example.second.model.Task;
import com.example.second.service.TaskService;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    private String getUserId(){
        return ((User)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
    }

    private boolean isAdmin() {
        return SecurityContextHolder.getContext().getAuthentication()
        .getAuthorities()
        .stream()
        .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }


    // Create task
    @PostMapping("/")
    public ResponseEntity<?> create(@RequestBody Task task) {
        task.setId(UUID.randomUUID().toString());
        task.setOwnerId(getUserId());
        taskService.createTask(task);
        return ResponseEntity.ok("Task created");
    }

    // Get ALL Tasks (admin only)
    @GetMapping("/")
    public ResponseEntity<?> getAllTasks() {
        if(!isAdmin()){
            return ResponseEntity.status(403).body("Only admin can view all tasks");
        }
        List<TaskDetails> tasks=taskService.getAllTasks();
        return ResponseEntity.ok(tasks);
    }
    
    // Get single task
    @GetMapping("/{id}")
    public ResponseEntity<?> getTask(@PathVariable String id) {
        TaskDetails dto=taskService.getTaskById(id);
        if(dto==null){ 
            return ResponseEntity.status(404).body("Task not found");
        }

        if(!isAdmin() && !dto.getOwnerId().equals(getUserId())){
            return ResponseEntity.status(403).body("You can view only your own tasks");
        }
        return ResponseEntity.ok(dto);
    }
    
    // Get my tasks
    @GetMapping("/my")
    public ResponseEntity<?> getMyTasks(){
        String userId=getUserId();
        List<Task> tasks=taskService.getTasksByOwner(userId);
        if(tasks.isEmpty()){    
            return ResponseEntity.status(404).body("No tasks found");
        }
        return ResponseEntity.ok(tasks);
    }

    // // List tasks for owner
    // @GetMapping("/owner/{ownerId}")
    // public ResponseEntity<List<Task>> getUserTasks(@PathVariable String ownerId) {
    //     List<Task> tasks=taskService.getTasksByOwner(ownerId);
    //     if(tasks.isEmpty()){    
    //         return ResponseEntity.status(404).body(null);
    //     }
    //     if(!isAdmin() && !ownerId.equals(getUserId())){
    //         return ResponseEntity.status(403).body(null);
    //     }
    //     return ResponseEntity.ok(tasks);
    // }

    // Update
    @PutMapping("/{id}")
    public ResponseEntity<?> updateTask(@PathVariable String id, @RequestBody Task task) {
        TaskDetails existingTask=taskService.getTaskById(id);
        if(existingTask==null){ 
            return ResponseEntity.status(404).body("Task not found");
        }
        if(!isAdmin() && !existingTask.getOwnerId().equals(getUserId())){
            return ResponseEntity.status(403).body("You can update only your own tasks");
        }
        task.setId(id);
        task.setOwnerId(existingTask.getOwnerId()); // preserve owner
        taskService.updateTask(task);
        return ResponseEntity.ok("Task updated");
    }

    // Delete
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTask(@PathVariable String id) {
        TaskDetails existingTask=taskService.getTaskById(id);
        if(existingTask==null){
            return ResponseEntity.status(404).body("Task not found");
        }
        if(!isAdmin() && !existingTask.getOwnerId().equals(getUserId())){
            return ResponseEntity.status(403).body("You can delete only your own tasks");
        }

        taskService.deleteTask(id);
        return ResponseEntity.ok("Task deleted");
    }
}
