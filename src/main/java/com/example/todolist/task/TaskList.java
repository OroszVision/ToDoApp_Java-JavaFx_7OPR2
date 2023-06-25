package com.example.todolist.task;

import com.example.todolist.AbstractList;
import javafx.concurrent.Task;

public class TaskList extends AbstractList {
    public TaskList() {
        super();
    }

    @Override
    public void updateTask(Task oldTask, Task newTask) {
        int index = tasks.indexOf(oldTask);
        tasks.set(index, newTask);
    }
    public void removeAll() {
        tasks.clear();
    }
}
