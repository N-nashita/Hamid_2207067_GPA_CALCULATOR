package com.example.gpa.controller;

import com.example.gpa.model.Course;
import com.example.gpa.util.Navigation;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

public class ResultController {
    @FXML private Label lblGpaCombined;
    @FXML private Label lblTotalCredits;
    @FXML private TableView<Course> table;
    @FXML private TableColumn<Course, String> colName;
    @FXML private TableColumn<Course, String> colCode;
    @FXML private TableColumn<Course, Double> colCredit;
    @FXML private TableColumn<Course, String> colTeacher1;
    @FXML private TableColumn<Course, String> colTeacher2;
    @FXML private TableColumn<Course, String> colGrade;

    private ObservableList<Course> courses;

    public void setCourses(ObservableList<Course> courses) {
        this.courses = courses;
        updateView();
    }

    private void updateView() {
        if (courses == null) return;
        if (table != null) {
            if (colName.getCellValueFactory() == null) {
                colName.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("name"));
                colCode.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("code"));
                colCredit.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("credit"));
                colTeacher1.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("teacher1"));
                colTeacher2.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("teacher2"));
                colGrade.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("grade"));
            }
            table.setItems(courses);
        }

        double gpa = calculateGPA(courses);
        if (lblGpaCombined != null) {
            lblGpaCombined.setText("Your GPA is " + String.format("%.2f", gpa));
        }

        if (lblTotalCredits != null) {
            double sum = courses.stream().mapToDouble(Course::getCredit).sum();
            lblTotalCredits.setText(String.format("Total Credits: %.1f", sum));
        }
    }

    private double calculateGPA(ObservableList<Course> list) {
        if (list == null || list.isEmpty()) return 0.0;
        Map<String, Double> map = gradeMap();
        double totalPoints = 0.0;
        double totalCredits = 0.0;
        for (Course c : list) {
            double gp = map.getOrDefault(c.getGrade(), 0.0);
            totalPoints += gp * c.getCredit();
            totalCredits += c.getCredit();
        }
        if (totalCredits == 0) return 0.0;
        return totalPoints / totalCredits;
    }

    private Map<String, Double> gradeMap() {
        Map<String, Double> m = new HashMap<>();
        m.put("A+", 4.0);
        m.put("A", 3.75);
        m.put("A-", 3.5);
        m.put("B+", 3.25);
        m.put("B", 3.0);
        m.put("B-", 2.75);
        m.put("C+", 2.5);
        m.put("C", 2.25);
        m.put("D", 2.0);
        m.put("F", 0.0);
        return m;
    }

    @FXML
    private void onBack(ActionEvent e) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/gpa/home.fxml"));
        Parent root = loader.load();
        Navigation.setRoot(e, root);
    }
}
