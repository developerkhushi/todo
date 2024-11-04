package com.khushi.todo.services;

import com.khushi.todo.entities.Todo;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class FileService {
    private static final String FILE_PATH = "todos.txt";

    public FileService() {
        try {
            Path path = Paths.get(FILE_PATH);
            if (Files.notExists(path)) {
                Files.createFile(path);
            }
        } catch (IOException e) {
            System.out.println("An error occurred while creating the file" + e.getMessage());
        }
    }

    public List<Todo> readTodos() throws IOException {
        Path path = Paths.get(FILE_PATH);
        if (Files.notExists(path)) {
            return new ArrayList<>();
        }

        String content = Files.readString(Path.of(FILE_PATH));
        if (content.isEmpty()) {
            return new ArrayList<>();
        }

        // Split the content by lines and map each line to a Todo object
        return Arrays.stream(content.split("\n"))
                .map(line -> {
                    String[] parts = line.split(",", 3);
                    int id = Integer.parseInt(parts[0].trim());
                    String title = parts[1].trim();
                    // Split the remaining part (parts[2]) into description and completed flag
                    int lastCommaIndex = parts[2].lastIndexOf(',');
                    String description = parts[2].substring(0, lastCommaIndex).trim();
                    if (!description.startsWith("\"") && !description.endsWith("\"")) {
                        description = "\"" + description + "\"";
                    }
                    boolean completed = Boolean.parseBoolean(parts[2].substring(lastCommaIndex + 1).trim());
                    return new Todo(id, title, description, completed);
                })
                .collect(Collectors.toList());
    }

    public void writeTodo(List<Todo> todos) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (Todo todo : todos) {
                if (todo.getDescription().startsWith("\"") && todo.getDescription().endsWith("\"")) {
                    writer.write(todo.toString());
                }
                else {
                    writer.write(formatTodoForFile(todo));
                }
                writer.newLine();
            }
        }
    }

    private static String formatTodoForFile(Todo todo) {
        // Format the todo in the correct format for saving: id,title,"description",completed
        return String.format("%d,%s,\"%s\",%b",
                todo.getId(),
                todo.getTitle(),
                todo.getDescription(),
                todo.isCompleted());
    }
}