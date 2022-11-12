package com.fastcampus.kotlinspring.todo.service

import com.fastcampus.kotlinspring.todo.api.model.TodoRequest
import com.fastcampus.kotlinspring.todo.domain.Todo
import com.fastcampus.kotlinspring.todo.domain.TodoRepository
import org.springframework.data.domain.Sort
import org.springframework.data.domain.Sort.Direction
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import java.time.LocalDateTime

@Service
class TodoService(
    private val todoRepository: TodoRepository,
) {

    @Transactional(readOnly = true)
    fun findAll(): List<Todo> =
        todoRepository.findAll(Sort.by(Direction.DESC, "id"))

    @Transactional(readOnly = true)
    fun findById(id: Long) =
        todoRepository.findByIdOrNull(id)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)

    @Transactional
    fun create(request: TodoRequest?): Todo {
        checkNotNull(request) { "Todo Request is Null" }

        return todoRepository.save(
            Todo(
                title = request.title,
                description = request.description,
                done = false,
                createdAt = LocalDateTime.now(),
            )
        )
    }

    @Transactional
    fun update(id: Long, request: TodoRequest?): Todo {
        checkNotNull(request) { "Todo Request is Null" }

        return findById(id).let {
                it.update(request.title, request.description, request.done)
                todoRepository.save(it)
        }
    }

    @Transactional
    fun delete(id: Long): Unit = todoRepository.deleteById(id)
}