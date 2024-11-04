package com.khushi.todo;

import com.khushi.todo.controller.TodoController;

public class Main {
    public static void main(String[] args) {
        TodoController taskController = new TodoController();
        taskController.showMenu();
    }
}