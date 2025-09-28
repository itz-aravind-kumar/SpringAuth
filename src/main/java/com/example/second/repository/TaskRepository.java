package com.example.second.repository;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.second.dto.TaskDetails;
import com.example.second.model.Task;

@Repository
public class TaskRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // CREATE: Add new task with UUID
    public int save(Task task) {
        String sql = "INSERT INTO tasks (id, title, description, owner_id, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?)";
        Timestamp now = new Timestamp(System.currentTimeMillis());
        return jdbcTemplate.update(sql, task.getId(), task.getTitle(), task.getDescription(), task.getOwnerId(), now, now);
    }

    // READ: Get by task ID
    public TaskDetails findById(String taskId) {
    String sql = "SELECT t.id as task_id, t.title,t.description,t.created_at, u.username, u.email, t.owner_id FROM tasks t JOIN users u ON t.owner_id = u.id WHERE t.id = ?";
    return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
        TaskDetails dto = new TaskDetails();
        dto.setTaskId(rs.getString("task_id"));
        dto.setTitle(rs.getString("title"));
        dto.setDescription(rs.getString("description"));
        dto.setCreatedAt(rs.getString("created_at"));
        dto.setUsername(rs.getString("username"));
        dto.setEmail(rs.getString("email"));
        dto.setOwnerId(rs.getString("owner_id"));
        return dto;
    }, taskId);
}

    // READ: List tasks for a user
    public List<Task> findByOwner(String userId) {
        String sql = "SELECT * FROM tasks WHERE owner_id = ?";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Task.class), userId);
    }

    // READ: List all tasks
    public List<TaskDetails> findAll() {
        String sql = "SELECT t.id, t.title, t.description, t.created_at, u.username, u.email " +
                    "FROM tasks t JOIN users u ON t.owner_id = u.id";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            TaskDetails dto = new TaskDetails();
            dto.setTaskId(rs.getString("id"));
            dto.setTitle(rs.getString("title"));
            dto.setDescription(rs.getString("description"));
            dto.setCreatedAt(rs.getString("created_at"));
            dto.setUsername(rs.getString("username"));
            dto.setEmail(rs.getString("email"));
            return dto;
        });
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