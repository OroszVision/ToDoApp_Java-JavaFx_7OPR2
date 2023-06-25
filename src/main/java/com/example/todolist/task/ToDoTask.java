package com.example.todolist.task;

import javafx.concurrent.Task;

import java.io.Serializable;

public class ToDoTask extends Task implements Serializable {
    private String taskName;

    public ToDoTask(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    @Override
    public String toString() {
        return taskName;
    }

    @Override
    protected Object call() throws Exception {
        return null;
    }

}
