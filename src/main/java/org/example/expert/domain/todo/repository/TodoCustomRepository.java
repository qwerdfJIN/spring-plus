package org.example.expert.domain.todo.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import org.example.expert.domain.todo.entity.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TodoCustomRepository {

	Page<Todo> findAllByWeatherAndModifiedAtRange(String weather, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
	Optional<Todo> findByIdWithUser(Long todoId);
}
