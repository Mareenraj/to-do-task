package com.todotask.backend.controller;

import com.todotask.backend.dto.TaskRequest;
import com.todotask.backend.dto.TaskResponse;
import com.todotask.backend.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/task")
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public ResponseEntity<String> addTask(@Valid @RequestBody TaskRequest taskRequest) {
        String successMessage = taskService.addTask(taskRequest);
        return ResponseEntity.ok(successMessage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> getTaskById(@PathVariable Long id) {
        TaskResponse taskResponse = taskService.getTaskById(id);
        return ResponseEntity.ok(taskResponse);
    }

    @GetMapping
    public ResponseEntity<List<TaskResponse>> getAllTasks() {
        List<TaskResponse> taskResponseList = taskService.findAll();
        return ResponseEntity.ok(taskResponseList);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateTaskStatus(@PathVariable Long id, @RequestParam boolean completed) {
        String successMessage = taskService.updateTaskStatus(id, completed);
        return ResponseEntity.ok(successMessage);
    }
}
