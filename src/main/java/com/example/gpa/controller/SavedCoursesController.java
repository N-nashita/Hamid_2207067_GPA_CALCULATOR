package com.example.gpa.controller;

import com.example.gpa.db.CourseRepository;
import com.example.gpa.model.Course;
import com.example.gpa.util.Navigation;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class SavedCoursesController {

    @FXML private TableView<Course> table;
    @FXML private TableColumn<Course, String> colName;
    @FXML private TableColumn<Course, String> colCode;
    @FXML private TableColumn<Course, Double> colCredit;
    @FXML private TableColumn<Course, String> colTeacher1;
    @FXML private TableColumn<Course, String> colTeacher2;
    @FXML private TableColumn<Course, String> colGrade;

    private final CourseRepository repository = new CourseRepository();

    @FXML
    public void initialize() {
        if (colName.getCellValueFactory() == null) {
            colName.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("name"));
            colCode.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("code"));
            colCredit.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("credit"));
            colTeacher1.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("teacher1"));
            colTeacher2.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("teacher2"));
            colGrade.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("grade"));
        }
        loadCourses();
    }

    private void loadCourses() {
        try {
            repository.init();
            ObservableList<Course> all = repository.findAll();
            table.setItems(all);
        } catch (Exception ex) {
            showAlert("Failed to load courses: " + ex.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void onDeleteSelected(ActionEvent e) {
        Course selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Please select a course to delete.", Alert.AlertType.WARNING);
            return;
        }
        try {
            boolean ok = repository.deleteByCode(selected.getCode());
            if (!ok) {
                showAlert("Delete failed in database.", Alert.AlertType.ERROR);
                return;
            }
            table.getItems().remove(selected);
            table.getSelectionModel().clearSelection();
            showAlert("Course deleted.", Alert.AlertType.INFORMATION);
        } catch (Exception ex) {
            showAlert("DB delete error: " + ex.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void onBackHome(ActionEvent e) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/gpa/home.fxml"));
            Parent root = loader.load();
            Navigation.setRoot(e, root);
        } catch (Exception ex) {
            showAlert("Navigation error: " + ex.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void showAlert(String msg, Alert.AlertType type) {
        Alert a = new Alert(type, msg, ButtonType.OK);
        a.showAndWait();
    }
}