module com.example.todolist {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.todolist to javafx.fxml;
    exports com.example.todolist;
    exports com.example.todolist.interfaces;
    opens com.example.todolist.interfaces to javafx.fxml;
    exports com.example.todolist.task;
    opens com.example.todolist.task to javafx.fxml;

}