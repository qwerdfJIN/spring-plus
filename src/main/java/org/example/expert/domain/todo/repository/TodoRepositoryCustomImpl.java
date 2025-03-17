package org.example.expert.domain.todo.repository;

import static org.example.expert.domain.comment.entity.QComment.*;
import static org.example.expert.domain.manager.entity.QManager.*;
import static org.example.expert.domain.todo.entity.QTodo.*;
import static org.example.expert.domain.user.entity.QUser.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.example.expert.domain.todo.dto.response.TodoSearchResponse;
import org.example.expert.domain.todo.entity.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TodoRepositoryCustomImpl implements TodoRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	@Override
	public Page<Todo> findAllByWeatherAndModifiedAtRange(
		String weather, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable
	) {
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
		return new PageImpl<>(results, pageable, total != null ? total : 0L);
	}

	@Override
	public Page<TodoSearchResponse> findAllByTitleAndNicknameAndCreatedAtRange(
		String title, String nickname, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable
	) {
		BooleanBuilder builder = new BooleanBuilder();
		if (title != null && !title.isEmpty()) {
			builder.and(todo.title.containsIgnoreCase(title));
		}
		if (nickname != null && !nickname.isEmpty()) {
			builder.and(manager.user.nickname.containsIgnoreCase(nickname));
		}
		if (startDate != null) {
			builder.and(todo.createdAt.goe(startDate));
		}
		if (endDate != null) {
			builder.and(todo.createdAt.loe(endDate));
		}

		List<TodoSearchResponse> results = queryFactory.select(
			Projections.constructor(
				TodoSearchResponse.class,
				todo.title,
				manager.id.countDistinct(),
				comment.id.countDistinct()
			)
		)
			.from(todo)
			.leftJoin(todo.managers, manager)
			.leftJoin(manager.user, user)
			.leftJoin(todo.comments, comment)
			.where(builder)
			.groupBy(todo.id, todo.title, todo.createdAt)
			.orderBy(todo.createdAt.desc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		Long total = queryFactory.select(todo.countDistinct())
			.from(todo)
			.leftJoin(todo.managers, manager)
			.leftJoin(manager.user, user)
			.where(builder)
			.fetchOne();
		return new PageImpl<>(results, pageable, total != null ? total : 0L);
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