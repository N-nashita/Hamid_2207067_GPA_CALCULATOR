package com.example.gpa.controller;

import com.example.gpa.util.Navigation;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

public class HomeController {

    @FXML
    private void onStart(ActionEvent event) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/gpa/course_entry.fxml"));
        Parent root = loader.load();
        Navigation.setRoot(event, root);
    }
    
    @FXML
    public void onViewSaved(ActionEvent e) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/gpa/saved_courses.fxml"));
        Parent root = loader.load();
        Navigation.setRoot(e, root);
    }
}
