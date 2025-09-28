package com.example.second.repository;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import com.example.second.model.User;

@Repository
public class UserRepository {
    
    @Autowired
    private JdbcTemplate jdbcTemplate;

    // CREATE: Register a user with UUID
    public int save(User user) {
        String sql = "INSERT INTO users (id, username, email, password, role) VALUES (?, ?, ?, ?, ?)";
        String uuid = UUID.randomUUID().toString();
        user.setId(uuid);
        user.setRole("USER"); // Default role
        return jdbcTemplate.update(sql, uuid, user.getUsername(), user.getEmail(), user.getPassword(), user.getRole());
    }

    // READ: Find by username
    public User findByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(User.class), username);
    }

    // READ: Find by ID
    public User findById(String id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(User.class), id);
    }

    // READ: List all users
    public List<User> findAll() {
        String sql = "SELECT * FROM users";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(User.class));
    }

    // UPDATE: Change password or role
    public int update(String id,User user) {
        System.out.println("Attempting to update user with ID: " + id);
        System.out.println("Email: " + user.getEmail());
        System.out.println("Username: " + user.getUsername());
        // System.out.println("Password: " + user.getPassword());

        String sql = "UPDATE users SET email=?, password = ?, username=? WHERE id = ?";
        return jdbcTemplate.update(sql, user.getEmail(),user.getPassword(), user.getUsername(), id);
    }

    // DELETE: Remove a user
    public int deleteById(String id) {
        String sql = "DELETE FROM users WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }

    public User findByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";
        return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(User.class), email);
    }
}