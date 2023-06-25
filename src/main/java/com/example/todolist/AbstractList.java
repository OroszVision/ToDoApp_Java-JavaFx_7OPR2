package com.example.todolist;

import javafx.concurrent.Task;

import java.util.ArrayList;

public abstract class AbstractList {
    protected ArrayList<Task> tasks;

    public AbstractList() {
        tasks = new ArrayList<>();
    }

    public void addTask(Task task) {
        tasks.add(task);
    }

    public void removeTask(Task task) {
        tasks.remove(task);
    }

    public  ArrayList<Task> getTasks() {
        return tasks;
    }

    public abstract void updateTask(Task oldTask, Task newTask);
}
