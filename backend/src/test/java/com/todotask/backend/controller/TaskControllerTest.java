package com.todotask.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.todotask.backend.dto.TaskRequest;
import com.todotask.backend.dto.TaskResponse;
import com.todotask.backend.exception.AlreadyUncompletedException;
import com.todotask.backend.exception.ResourceNotFoundException;
import com.todotask.backend.service.TaskService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TaskController.class)
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TaskService taskService;

    private ObjectMapper objectMapper;

    @Test
    void addTask_WhenValidRequest_ShouldReturnSuccessMessageWithOkStatus() throws Exception {
        objectMapper = new ObjectMapper();
        TaskRequest taskRequest = new TaskRequest("Test Title", "Test Description");
        String successMessage = "Task created successfully";
        when(taskService.addTask(taskRequest)).thenReturn(successMessage);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/task")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskRequest)))
                .andExpect(status().isOk())
                .andExpect(content().string(successMessage));
    }

    @Test
    void addTask_WhenInvalidRequest_ShouldReturnValidationError() throws Exception {
        objectMapper = new ObjectMapper();
        TaskRequest invalidRequest = new TaskRequest("", null);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/task")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());  // 400 due to @Valid
    }

    @Test
    void getAllTasks_WhenServiceReturnsRecentTasks_ShouldReturnListWithOkStatus() throws Exception {
        objectMapper = new ObjectMapper();
        TaskResponse taskResponse = new TaskResponse(1L, "Test Title", "Test Description", false);
        List<TaskResponse> mockTasks = List.of(taskResponse);
        when(taskService.findRecentIncomplete()).thenReturn(mockTasks);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/task"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].title").value("Test Title"));
    }

    @Test
    void getAllTasks_WhenNoTasks_ShouldReturnEmptyListWithOkStatus() throws Exception {
        when(taskService.findRecentIncomplete()).thenReturn(List.of());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/task"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(0));
    }

    @Test
    void updateTaskStatus_WhenValidIdAndCompletedTrue_ShouldReturnSuccessMessageWithOkStatus() throws Exception {
        objectMapper = new ObjectMapper();
        String successMessage = "Task status updated successfully";
        when(taskService.updateTaskStatus(1L, true)).thenReturn(successMessage);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/task/{id}", 1L)
                        .param("completed", "true"))
                .andExpect(status().isOk())
                .andExpect(content().string(successMessage));
    }

    @Test
    void updateTaskStatus_WhenCompletedFalse_ShouldThrowAlreadyUncompletedException() throws Exception {
        when(taskService.updateTaskStatus(1L, false))
                .thenThrow(new AlreadyUncompletedException("Task with ID 1 is already uncompleted."));

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/task/{id}", 1L)
                        .param("completed", "false"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("already uncompleted")));
    }

    @Test
    void updateTaskStatus_WhenTaskNotFound_ShouldThrowResourceNotFoundException() throws Exception {
        when(taskService.updateTaskStatus(1L, true))
                .thenThrow(new ResourceNotFoundException("No uncompleted task found with ID: 1"));

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/task/{id}", 1L)
                        .param("completed", "true"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("No uncompleted task found")));
    }
}