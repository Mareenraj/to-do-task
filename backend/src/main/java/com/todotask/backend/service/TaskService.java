package com.todotask.backend.service;

import com.todotask.backend.dto.TaskRequest;
import com.todotask.backend.dto.TaskResponse;
import com.todotask.backend.entity.Task;
import com.todotask.backend.exception.AlreadyUncompletedException;
import com.todotask.backend.exception.ResourceNotFoundException;
import com.todotask.backend.mapper.TaskMapper;
import com.todotask.backend.repository.TaskRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {
    private final TaskRepository taskRepository;

    private final TaskMapper taskMapper;

    public TaskService(TaskRepository taskRepository, TaskMapper taskMapper) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
    }

    @Transactional
    public String addTask(TaskRequest taskRequest) {
        Task task = taskMapper.mapToEntity(taskRequest);
        taskRepository.save(task);
        return "Task created successfully";
    }

    public TaskResponse getTaskById(Long id) {
        Task task = taskRepository.findTaskByIdAndCompletedIsFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("No uncompleted task found with ID: " + id));
        return taskMapper.mapToResponse(task);
    }

    public List<TaskResponse> findAll() {
        List<Task> taskList = taskRepository.findAllByCompletedIsFalse();
        return taskMapper.mapToResponseList(taskList);
    }

    @Transactional
    public String updateTaskStatus(Long id, boolean completed) {
        if (!completed) {
            throw new AlreadyUncompletedException("Task with ID " + id + " is already uncompleted.");
        }
        Task task = taskRepository.findTaskByIdAndCompletedIsFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("No uncompleted task found with ID: " + id));
        task.setCompleted(true);
        taskRepository.save(task);
        return "Task status updated successfully";
    }
}
