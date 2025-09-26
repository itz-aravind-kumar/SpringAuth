package com.example.second.repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.second.model.Task;

@Repository
public class TaskRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // CREATE: Add new task with UUID
    public int save(Task task) {
        String sql = "INSERT INTO tasks (id, title, description, owner_id, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?)";
        String uuid = UUID.randomUUID().toString();
        task.setId(uuid);
        Timestamp now = new Timestamp(System.currentTimeMillis());
        return jdbcTemplate.update(sql,
            uuid, task.getTitle(), task.getDescription(), task.getOwnerId(), now, now);
    }

    // READ: Get by task ID
    public Task findById(String id) {
        String sql = "SELECT * FROM tasks WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(Task.class), id);
    }

    // READ: List tasks for a user
    public List<Task> findByOwner(String userId) {
        String sql = "SELECT * FROM tasks WHERE owner_id = ?";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Task.class), userId);
    }

    // READ: List all tasks
    public List<Task> findAll() {
        String sql = "SELECT * FROM tasks";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Task.class));
    }

    // UPDATE: Update a task (title, desc, updated time)
    public int update(Task task) {
        String sql = "UPDATE tasks SET title = ?, description = ?, updated_at = ? WHERE id = ?";
        Timestamp now = new Timestamp(System.currentTimeMillis());
        return jdbcTemplate.update(sql, task.getTitle(), task.getDescription(), now, task.getId());
    }

    // DELETE: Delete a task
    public int deleteById(String id) {
        String sql = "DELETE FROM tasks WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }
}