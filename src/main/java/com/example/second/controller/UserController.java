package com.example.second.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.second.model.User;
import com.example.second.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    private String getUserId(){
        return ((User)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
    }

    private boolean isAdmin() {
        return SecurityContextHolder.getContext().getAuthentication()
        .getAuthorities()
        .stream()
        .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }


    // List all users (admin only, will restrict with JWT/roles later)
    @GetMapping("/")
    public ResponseEntity<?> allUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    // Get user by userid
    @GetMapping("/{id}")
    public ResponseEntity<?> user(@PathVariable String id) {
        User user=userService.getUserById(id);
        if(user==null){ 
            return ResponseEntity.status(404).body("User not found");
        }
        if(!isAdmin() && !getUserId().equals(id)){
            return ResponseEntity.status(403).body("You can view only your own profile");
        }
        return ResponseEntity.ok(user);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable String id, @RequestBody User updatedUser) {
        User user = userService.getUserById(id);         // 1. Fetch resource
        if (user == null) {
            return ResponseEntity.status(404).body("User not found");  // 2. Not found
        }
        boolean isAdmin = isAdmin();
        String requesterId = getUserId();
        if (!isAdmin && !requesterId.equals(user.getId())) {  // 3. Authorization based on resource data
            return ResponseEntity.status(403).body("You can update only your own profile");
        }
        userService.updateUser(id, updatedUser);         // 4. Update only if allowed
        return ResponseEntity.ok("User updated");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable String id) {
        User user = userService.getUserById(id);         // 1. Fetch resource
        if (user == null) {
            return ResponseEntity.status(404).body("User not found");  // 2. Not found
        }
        boolean isAdmin = isAdmin();
        String requesterId = getUserId();
        if (!isAdmin && !requesterId.equals(user.getId())) { // 3. Authorization based on resource data
            return ResponseEntity.status(403).body("You can delete only your own profile");
        }
        userService.deleteUser(id);                      // 4. Delete only if allowed
        return ResponseEntity.ok("User deleted");
    }
}
