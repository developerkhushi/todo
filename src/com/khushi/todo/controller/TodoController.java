package com.khushi.todo.controller;

import com.khushi.todo.entities.Todo;
import com.khushi.todo.services.TodoService;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class TodoController {
    private final TodoService todoService;
    private final Scanner scanner;

    public TodoController() {
        this.todoService = new TodoService();
        this.scanner = new Scanner(System.in);
    }

    // Main menu to display options
    public void showMenu() {
        System.out.println("Welcome to the Todo Task Manager!");
        while (true) {
            System.out.println("\nChoose an option:");
            System.out.println("1. Add Todo");
            System.out.println("2. Delete Todo");
            System.out.println("3. Update Todo");
            System.out.println("4. Get Todo by ID");
            System.out.println("5. Get All Todos");
            System.out.println("6. Exit");

            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            try {
                switch (choice) {
                    case 1 -> addTodo();
                    case 2 -> deleteTodo();
                    case 3 -> updateTodo();
                    case 4 -> getTodoById();
                    case 5 -> getAllTodos();
                    case 6 -> {
                        System.out.println("Exiting...");
                        return;
                    }
                    default -> System.out.println("Invalid choice. Please try again.");
                }
            } catch (IOException e) {
                System.out.println("An error occurred: " + e.getMessage());
            }
        }
    }

    // Adds a new todo
    private void addTodo() throws IOException {
        System.out.print("Enter title: ");
        String title = scanner.nextLine();
        System.out.print("Enter description: ");
        String description = scanner.nextLine();
        System.out.print("Is it completed? (true/false): ");
        boolean completed = scanner.nextBoolean();
        scanner.nextLine(); // consume newline

        int id = generateId(); // Use an ID generation logic as needed
        Todo todo = new Todo(id, title, description, completed);
        todoService.addTodos(todo);
        System.out.println("Todo added successfully.");
    }

    // Deletes a todo by ID
    private void deleteTodo() throws IOException {
        System.out.print("Enter ID of the Todo to delete: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // consume newline

        if (todoService.deleteTodo(id)) {
            System.out.println("Todo deleted successfully.");
        } else {
            System.out.println("Todo not found.");
        }
    }

    // Updates an existing todo by ID
    private void updateTodo() throws IOException {
        System.out.print("Enter ID of the Todo to update: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // consume newline

        System.out.print("Enter new title: ");
        String title = scanner.nextLine();
        System.out.print("Enter new description: ");
        String description = scanner.nextLine();
        System.out.print("Is it completed? (true/false): ");
        boolean completed = scanner.nextBoolean();
        scanner.nextLine(); // consume newline

        if (todoService.updateTodos(id, title, description, completed)) {
            System.out.println("Todo updated successfully.");
        } else {
            System.out.println("Todo not found.");
        }
    }

    // Gets a single todo by ID
    private void getTodoById() throws IOException {
        System.out.print("Enter ID of the Todo to retrieve: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // consume newline

        Optional<Todo> todo = todoService.getTodoById(id);
        todo.ifPresentOrElse(
                System.out::println,
                () -> System.out.println("Todo not found.")
        );
    }

    // Gets and displays all todos
    private void getAllTodos() throws IOException {
        List<Todo> todos = todoService.getAllTodos();
        if (todos.isEmpty()) {
            System.out.println("No todos found.");
        } else {
            todos.forEach(System.out::println);
        }
    }

    // Generates a unique ID for new todos (simple increment strategy)
    private int generateId() throws IOException {
        List<Todo> todos = todoService.getAllTodos();
        return todos.isEmpty() ? 1 : todos.stream().mapToInt(Todo::getId).max().getAsInt() + 1;
    }
}
