package com.example.todolist.task;

import com.example.todolist.FileHandler;
import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Optional;

public class TaskGUI extends Application {
    private TaskList taskList;
    private FileHandler fileHandler;
    private final static String FILENAME = "tasks.txt";

    @Override
    public void start(Stage stage) {
        // initialization of taskList and fileHandler
        taskList = new TaskList();
        fileHandler = new FileHandler(taskList);

        // create GUI elements
        ListView<Task> taskListView = new ListView<>();
        Button addButton = new Button("Add Task");
        Button updateButton = new Button("Update Task");
        Button deleteButton = new Button("Delete Task");
        Button importButton = new Button("Load Tasks");
        Button exportButton = new Button("Save Tasks");

        File file = new File("saved_files/" + FILENAME );
        if(!file.exists()){
            importButton.setDisable(true);
        }

        stage.setResizable(false);

        // actions for buttons
        addButton.setOnAction(event -> createNewTask(taskListView, exportButton));

        updateButton.setOnAction(event -> updateTask(taskListView));

        deleteButton.setOnAction(event -> deleteTask(taskListView));

        importButton.setOnAction(event -> loadFromFile(taskListView, importButton, exportButton));

        exportButton.setOnAction(event -> overwriteOrMergeTasks(taskListView, importButton, exportButton));

        GridPane gridPane = appView(taskListView, addButton, deleteButton, updateButton, importButton, exportButton);

        stage.setOnCloseRequest(event -> beforeQuitPrevention(event));


        // showing GUI
        show(stage, gridPane);
    }

    private void deleteTask(ListView<Task> taskListView) {
        Task selectedTask = taskListView.getSelectionModel().getSelectedItem();
        if (selectedTask != null) {
            taskList.removeTask(selectedTask);
            taskListView.getItems().remove(selectedTask);
        }
    }

    private void updateTask(ListView<Task> taskListView) {
        Task selectedTask = taskListView.getSelectionModel().getSelectedItem();
        if(selectedTask != null){
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Update Task");
            dialog.setHeaderText("Enter new task name:");
            dialog.setContentText("Task Name:");

            Optional<String> result = isNotNull(dialog);
            result.ifPresent(name -> {
                ToDoTask updatedTask = new ToDoTask(name);
                taskList.updateTask(selectedTask, updatedTask);

                // update the task list view
                int index = taskListView.getSelectionModel().getSelectedIndex();
                taskListView.getItems().set(index, updatedTask);
                taskListView.refresh();
            });
        }
    }

    private static Optional<String> isNotNull(TextInputDialog dialog) {
        Optional<String> result = dialog.showAndWait();
        while (result.isPresent() && result.get().trim().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Empty Name");
            alert.setHeaderText("The task name cannot be empty.");
            alert.setContentText("Please enter a non-empty task name.");
            alert.showAndWait();
            result = dialog.showAndWait();
        }
        return result;
    }

    private static GridPane appView(ListView<Task> taskListView, Button addButton, Button deleteButton, Button updateButton, Button importButton, Button exportButton) {
        // create layout for GUI elements
        GridPane gridPane = new GridPane();

        gridPane.setHgap(10); // Set horizontal spacing between cells
        gridPane.setVgap(10); // Set vertical spacing between cells
        Insets padding = new Insets(10);
        gridPane.setPadding(padding);

        gridPane.add(taskListView,0,0);
        gridPane.add(addButton,0,1);
        gridPane.add(updateButton,0,1);
        gridPane.add(deleteButton,0,1);
        gridPane.add(importButton,0,2);
        gridPane.add(exportButton,0,2);
        GridPane.setHalignment(addButton, HPos.CENTER);
        GridPane.setHalignment(deleteButton, HPos.RIGHT);
        GridPane.setHalignment(exportButton, HPos.RIGHT);
        return gridPane;
    }

    private void createNewTask(ListView<Task> taskListView, Button exportButton) {
        // create a new dialog for entering task details
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("New Task");
        dialog.setHeaderText("Enter the task name:");
        dialog.setContentText("Task Name:");

        // get the task name from the user and add it to the task list
        Optional<String> result = isNotNull(dialog);
        result.ifPresent(name -> {
            ToDoTask newTask = new ToDoTask(name);
            taskList.addTask(newTask);

            // update the task list view with the new task
            taskListView.getItems().add(newTask);
        });

        if(!taskListView.getItems().isEmpty()){
            exportButton.setDisable(false);
        }
    }

    private void loadFromFile(ListView<Task> taskListView, Button importButton, Button exportButton) {
        fileHandler.readFile(FILENAME);
        taskListView.getItems().clear();
        ArrayList<Task> tasks = taskList.getTasks();
        taskListView.getItems().addAll(tasks);
        importButton.setDisable(true);
        exportButton.setDisable(false);
    }

    private void saveToFile(ListView<Task> taskListView, Button importButton, Button exportButton) {
            fileHandler.writeFile(FILENAME);
            taskList.removeAll();
            taskListView.getItems().clear();
            importButton.setDisable(false);
            exportButton.setDisable(true);
    }

    private void overwriteOrMergeTasks(ListView<Task> taskListView, Button importButton, Button exportButton) {

        File file = new File("saved_files/" + FILENAME );
        if(file.exists() && !importButton.isDisabled()) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Overwrite?");
            alert.setHeaderText("Do you wanna overwrite your tasks? Click No to merge your tasks...");

            // add the buttons
            ButtonType buttonTypeYes = new ButtonType("Yes");
            ButtonType buttonTypeNo = new ButtonType("No");
            alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);

            // set the action for the no button
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == buttonTypeNo) {
                loadFromFile(taskListView, importButton, exportButton);
            }
        }
        saveToFile(taskListView,importButton,exportButton);
    }

    private static void beforeQuitPrevention(WindowEvent event) {// create the dialog
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Quit?");
        alert.setHeaderText("Do you wanna quit? Without saving all your changes will be discarded...");

        // add the buttons
        ButtonType buttonTypeYes = new ButtonType("Yes");
        ButtonType buttonTypeNo = new ButtonType("No");
        alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);

        // set the action for the yes button
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == buttonTypeNo) {
            event.consume();
        }
    }

    private static void show(Stage stage, GridPane gridPane) {
        // show GUI
        Scene scene = new Scene(gridPane);
        stage.setScene(scene);
        stage.show();
    }

}