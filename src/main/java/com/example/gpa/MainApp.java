package com.example.gpa;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Ensure database file exists and drop deprecated gpa_totals table
        try { 
            com.example.gpa.db.Database.init();
            com.example.gpa.db.Database.dropGpaTotals();
        } catch (Exception ignored) {}
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/gpa/home.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root, 1000, 650);
        primaryStage.setTitle("GPA Calculator");
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(900);
        primaryStage.setMinHeight(600);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
