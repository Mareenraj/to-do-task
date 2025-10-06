package com.todotask.backend.mapper;

import com.todotask.backend.dto.TaskRequest;
import com.todotask.backend.dto.TaskResponse;
import com.todotask.backend.entity.Task;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TaskMapperTest {
    @InjectMocks
    private TaskMapper taskMapper;

    @Test
    void mapToEntity_WhenValidRequest_ShouldReturnTaskWithTitleAndDescription() {
        TaskRequest taskRequest = new TaskRequest("Test Title", "Test Description");

        Task result = taskMapper.mapToEntity(taskRequest);

        assertNotNull(result);
        assertEquals("Test Title", result.getTitle());
        assertEquals("Test Description", result.getDescription());
        assertNull(result.getId());
        assertFalse(result.isCompleted());
        assertNull(result.getCreatedAt());
    }

    @Test
    void mapToEntity_WhenDescriptionIsNull_ShouldReturnTaskWithNullDescription() {
        taskMapper = new TaskMapper();
        TaskRequest taskRequest = new TaskRequest("Test Title", null);

        Task result = taskMapper.mapToEntity(taskRequest);

        assertNotNull(result);
        assertEquals("Test Title", result.getTitle());
        assertNull(result.getDescription());
        assertFalse(result.isCompleted());
    }

    @Test
    void mapToEntity_WhenTitleIsEmpty_ShouldReturnTaskWithEmptyTitle() {
        taskMapper = new TaskMapper();
        TaskRequest taskRequest = new TaskRequest("", "Empty Title Desc");

        Task result = taskMapper.mapToEntity(taskRequest);

        assertNotNull(result);
        assertEquals("", result.getTitle());
        assertEquals("Empty Title Desc", result.getDescription());
        assertFalse(result.isCompleted());
    }

    @Test
    void mapToResponse_WhenValidTask_ShouldReturnTaskResponseWithAllFields() {

        taskMapper = new TaskMapper();
        Task task = createMockTask(1L, false);

        TaskResponse result = taskMapper.mapToResponse(task);

        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals("Mock Task 1", result.title());
        assertEquals("Mock Desc 1", result.description());
    }

    @Test
    void mapToResponse_WhenDescriptionIsNull_ShouldReturnResponseWithNullDescription() {
        taskMapper = new TaskMapper();
        Task task = createMockTask(1L, false);
        task.setDescription(null);

        TaskResponse result = taskMapper.mapToResponse(task);

        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals("Mock Task 1", result.title());
        assertNull(result.description());
    }

    @Test
    void mapToResponse_WhenTaskIsCompleted_ShouldReturnTrueForCompleted() {
        taskMapper = new TaskMapper();
        Task task = createMockTask(1L, true);

        TaskResponse result = taskMapper.mapToResponse(task);

        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals("Mock Task 1", result.title());
    }

    @Test
    void mapToResponseList_WhenValidTaskList_ShouldReturnMappedResponseList() {
        taskMapper = new TaskMapper();
        List<Task> mockTasks = List.of(
                createMockTask(2L, false),
                createMockTask(1L, true)
        );

        List<TaskResponse> result = taskMapper.mapToResponseList(mockTasks);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Mock Task 2", result.get(0).title());
        assertEquals("Mock Task 1", result.get(1).title());
    }

    @Test
    void mapToResponseList_WhenEmptyTaskList_ShouldReturnEmptyList() {
        taskMapper = new TaskMapper();
        List<Task> emptyTasks = List.of();

        List<TaskResponse> result = taskMapper.mapToResponseList(emptyTasks);

        assertNotNull(result);
        assertTrue(result.isEmpty());
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
        return new TaskResponse(id, "Mock Task response " + id, "Mock Desc " + id, completed);
    }

}