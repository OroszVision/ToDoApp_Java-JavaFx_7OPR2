package com.example.todolist;

import com.example.todolist.interfaces.FileIO;
import com.example.todolist.task.TaskList;
import javafx.concurrent.Task;

import java.io.*;
import java.util.ArrayList;

public class FileHandler implements FileIO {
    private TaskList taskList;

    public FileHandler(TaskList taskList) {
        this.taskList = taskList;
    }

    @Override
    public void readFile(String fileName) {
        try {
            FileInputStream fileInputStream = new FileInputStream(new File("saved_files/" + fileName));
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

            ArrayList<Task> loadedTasks = (ArrayList<Task>) objectInputStream.readObject();

            for (Task loadedTask : loadedTasks) {
                if (!taskList.getTasks().contains(loadedTask)) {
                    taskList.addTask(loadedTask);
                }
            }
            objectInputStream.close();
            fileInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void writeFile(String fileName) {
        try {
            // Get the path of the project folder
            String projectPath = System.getProperty("user.dir");

            // Create a File object for the saved_files directory
            File savedFilesDirectory = new File(projectPath + "/saved_files");

            // Create the saved_files directory if it does not exist
            if (!savedFilesDirectory.exists()) {
                savedFilesDirectory.mkdir();
            }

            // Create a File object for the file in the saved_files directory
            File file = new File(savedFilesDirectory, fileName);

            // Create the file if it does not exist
            if (!file.exists()) {
                file.createNewFile();
            }
            // Get an OutputStream for the file
            OutputStream outputStream = new FileOutputStream(file);

            // Create an ObjectOutputStream to write the object to the OutputStream
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);

            // Write the entire task list to the file
            objectOutputStream.writeObject(taskList.tasks);

            // Close the ObjectOutputStream and the OutputStream
            objectOutputStream.close();
            outputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }




}
