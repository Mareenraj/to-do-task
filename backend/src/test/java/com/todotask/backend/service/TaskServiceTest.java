package com.todotask.backend.service;

import com.todotask.backend.dto.TaskRequest;
import com.todotask.backend.dto.TaskResponse;
import com.todotask.backend.entity.Task;
import com.todotask.backend.exception.AlreadyUncompletedException;
import com.todotask.backend.exception.ResourceNotFoundException;
import com.todotask.backend.mapper.TaskMapper;
import com.todotask.backend.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private TaskMapper taskMapper;

    @InjectMocks
    private TaskService taskService;

    private TaskRequest taskRequest;

    private Task task;

    @Test
    void addTask_WhenValidRequest_ShouldReturnSuccessMessage() {
        taskRequest = new TaskRequest(
                "Test Title",
                "Test Description");

        task = new Task();
        task.setId(1L);
        task.setTitle("Test Title");
        task.setDescription("Test Description");
        task.setCompleted(false);
        task.setCreatedAt(LocalDateTime.now());

        TaskResponse taskResponse = new TaskResponse(
                1L,
                "Test Title",
                "Test Description",
                false
        );

        when(taskMapper.mapToEntity(taskRequest)).thenReturn(task);
        when(taskRepository.save(task)).thenReturn(task);

        String result = taskService.addTask(taskRequest);

        assertEquals("Task created successfully", result);
    }

    @Test
    void findRecentIncomplete_WhenTasksExist_ShouldReturnTop5RecentIncomplete() {
        taskRequest = new TaskRequest(
                "Test Title",
                "Test Description");

        task = new Task();
        task.setId(1L);
        task.setTitle("Test Title");
        task.setDescription("Test Description");
        task.setCompleted(false);
        task.setCreatedAt(LocalDateTime.now());

        Pageable pageable = PageRequest.of(0, 5, Sort.by("createdAt").descending());
        List<Task> mockTasks = List.of(
                createMockTask(2L, false),
                createMockTask(1L, false)
        );
        List<TaskResponse> mockTaskResponses = List.of(
                createMockTaskResponse(2L, false),
                createMockTaskResponse(1L, false)
        );
        when(taskRepository.findByCompletedFalseOrderByCreatedAtDesc(pageable)).thenReturn(mockTasks);
        when(taskMapper.mapToResponseList(mockTasks)).thenReturn(mockTaskResponses);

        List<TaskResponse> result = taskService.findRecentIncomplete();

        assertEquals(2, result.size());
    }

    @Test
    void findRecentIncomplete_WhenNoIncompleteTasks_ShouldReturnEmptyList() {
        taskRequest = new TaskRequest(
                "Test Title",
                "Test Description");

        List<Task> mockTasks = List.of(
                createMockTask(2L, true),
                createMockTask(1L, true)
        );

        Pageable pageable = PageRequest.of(0, 5, Sort.by("createdAt").descending());
        when(taskRepository.findByCompletedFalseOrderByCreatedAtDesc(pageable)).thenReturn(mockTasks);
        when(taskMapper.mapToResponseList(mockTasks)).thenReturn(List.of());

        List<TaskResponse> result = taskService.findRecentIncomplete();

        assertTrue(result.isEmpty());
    }

    @Test
    void updateTaskStatus_WhenValidIdAndToComplete_ShouldUpdateAndReturnSuccess() {
        task = new Task();
        task.setId(1L);
        task.setTitle("Test Title");
        task.setDescription("Test Description");
        task.setCompleted(false);
        task.setCreatedAt(LocalDateTime.now());

        when(taskRepository.findTaskByIdAndCompletedIsFalse(1L)).thenReturn(Optional.of(task));

        String result = taskService.updateTaskStatus(1L, true);

        assertEquals("Task status updated successfully", result);
    }

    @Test
    void updateTaskStatus_WhenTaskNotFound_ShouldThrowResourceNotFoundException() {
        when(taskRepository.findTaskByIdAndCompletedIsFalse(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> taskService.updateTaskStatus(1L, true)
        );
        assertEquals("No uncompleted task found with ID: 1", exception.getMessage());
    }

    @Test
    void updateTaskStatus_WhenTaskAlreadyCompleted_ShouldThrowResourceNotFoundException() {
        when(taskRepository.findTaskByIdAndCompletedIsFalse(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> taskService.updateTaskStatus(1L, true)
        );
        assertEquals("No uncompleted task found with ID: 1", exception.getMessage());
    }

    @Test
    void updateTaskStatusWhenTryingToUncompleteShouldThrowAlreadyUncompletedException() {

        AlreadyUncompletedException exception = assertThrows(
                AlreadyUncompletedException.class,
                () -> taskService.updateTaskStatus(1L, false)
        );
        assertEquals("Task with ID 1 is already uncompleted.", exception.getMessage());
    }

    private Task createMockTask(Long id, boolean completed) {
        Task mockTask = new Task();
        mockTask.setId(id);
        mockTask.setTitle("Mock Task " + id);
        mockTask.setDescription("Mock Desc " + id);
        mockTask.setCompleted(completed);
        mockTask.setCreatedAt(LocalDateTime.now().minusDays(id));
        return mockTask;
    }

    private TaskResponse createMockTaskResponse(Long id, boolean completed) {
        return new TaskResponse(
                id,
                "Mock Task response " + id,
                "Mock Desc " + id,
                completed
        );
    }
}