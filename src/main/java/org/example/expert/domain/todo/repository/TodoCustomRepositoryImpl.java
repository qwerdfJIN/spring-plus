package org.example.expert.domain.todo.repository;

import static org.example.expert.domain.todo.entity.QTodo.*;
import static org.example.expert.domain.user.entity.QUser.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.example.expert.domain.todo.entity.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TodoCustomRepositoryImpl implements TodoCustomRepository {

	private final JPAQueryFactory queryFactory;

	@Override
	public Page<Todo> findAllByWeatherAndModifiedAtRange(
		String weather, LocalDateTime startDate, LocalDateTime endDate,
		Pageable pageable) {

		List<Todo> results = queryFactory.selectFrom(todo)
			.leftJoin(todo.user, user).fetchJoin()
			.where(
				weather == null ? null : todo.weather.eq(weather),
				startDate == null ? null : todo.modifiedAt.goe(startDate),
				endDate == null ? null : todo.modifiedAt.loe(endDate)
			)
			.orderBy(todo.modifiedAt.desc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		Long total = queryFactory.select(todo.count())
			.from(todo)
			.where(
				weather == null ? null : todo.weather.eq(weather),
				startDate == null ? null : todo.modifiedAt.goe(startDate),
				endDate == null ? null : todo.modifiedAt.loe(endDate)
			)
			.fetchOne();

		return new PageImpl<>(results, pageable, total == null ? 0 : total);
	}

	@Override
	public Optional<Todo> findByIdWithUser(Long todoId) {
		Todo result = queryFactory.selectFrom(todo)
			.leftJoin(todo.user, user).fetchJoin()
			.where(todo.id.eq(todoId))
			.fetchOne();
		return Optional.ofNullable(result);
	}
}