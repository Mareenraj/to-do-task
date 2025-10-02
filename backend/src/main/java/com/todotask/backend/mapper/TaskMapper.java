package com.todotask.backend.mapper;

import com.todotask.backend.dto.TaskRequest;
import com.todotask.backend.dto.TaskResponse;
import com.todotask.backend.entity.Task;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TaskMapper {
    public Task mapToEntity(TaskRequest taskRequest) {
        Task task = new Task();
        task.setTitle(taskRequest.title());
        task.setDescription(taskRequest.description());
        return task;
    }

    public TaskResponse mapToResponse(Task task) {
        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.isCompleted()
        );
    }

    public List<TaskResponse> mapToResponseList(List<Task> taskList) {
        return taskList.stream().map(this::mapToResponse).toList();
    }
}
