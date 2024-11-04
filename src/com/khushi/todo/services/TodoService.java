package com.khushi.todo.services;

import com.khushi.todo.entities.Todo;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class TodoService {

    private static final String FILE_PATH = "todos.txt";
    private final FileService fileService;

    public TodoService() {
        this.fileService = new FileService();
    }

    public void addTodos(Todo todo) throws IOException {
        List<Todo> todos = fileService.readTodos();
        todos.add(todo);
        fileService.writeTodo(todos);
    }

    public boolean deleteTodo(int id) throws IOException {
        List<Todo> todos = fileService.readTodos();
        boolean removed = todos.removeIf(todo -> todo.getId() == id);
        if (removed) {
            fileService.writeTodo(todos);
        }
        return removed;
    }

    public boolean updateTodos(int id, String title, String description, boolean completed) throws IOException {
        List<Todo> todos = fileService.readTodos();
        for (Todo todo : todos) {
            if (todo.getId() == id) {
                todo.setTitle(title);
                todo.setDescription(description);
                todo.setCompleted(completed);
                fileService.writeTodo(todos);
                return true;
            }
        }
        return false;
    }

    public Optional<Todo> getTodoById(int id) throws IOException{
        List<Todo> todos = fileService.readTodos();
        return todos.stream()
                .filter(todo -> todo.getId() == id)
                .findFirst();
    }

    public List<Todo> getAllTodos() throws IOException {
        return fileService.readTodos();
    }
}