package com.todotask.backend.repository;

import com.todotask.backend.entity.Task;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {
    Optional<Task> findTaskByIdAndCompletedIsFalse(Long id);

    List<Task> findByCompletedFalseOrderByCreatedAtDesc(Pageable pageable);
}
