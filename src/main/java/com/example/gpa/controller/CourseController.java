package com.example.gpa.controller;

import com.example.gpa.model.Course;
import com.example.gpa.util.Navigation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;

public class CourseController {

    @FXML private TextField courseName;
    @FXML private TextField courseCode;
    @FXML private TextField courseCredit;
    @FXML private TextField teacher1;
    @FXML private TextField teacher2;
    @FXML private ComboBox<String> grade;
    @FXML private TextField totalRequiredCredits;
    @FXML private Button btnAddCourse;
    @FXML private Button btnCalculate;
    @FXML private Label lblProgress;
    @FXML private TableView<Course> courseTable;
    @FXML private TableColumn<Course, String> colName;
    @FXML private TableColumn<Course, String> colCode;
    @FXML private TableColumn<Course, Double> colCredit;
    @FXML private TableColumn<Course, String> colTeacher1;
    @FXML private TableColumn<Course, String> colTeacher2;
    @FXML private TableColumn<Course, String> colGrade;
    @FXML private Button btnEditSelected;
    @FXML private Button btnDeleteSelected;

    private final ObservableList<Course> courses = FXCollections.observableArrayList();
    private Course editingCourse = null;

    @FXML
    public void initialize() {
        grade.getItems().addAll("A+","A","A-","B+","B","B-","C+","C","D","F");

        btnCalculate.setDisable(true);

        if (colName != null && colName.getCellValueFactory() == null) {
            colName.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("name"));
            colCode.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("code"));
            colCredit.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("credit"));
            colTeacher1.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("teacher1"));
            colTeacher2.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("teacher2"));
            colGrade.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("grade"));
        }
        if (courseTable != null) {
            courseTable.setItems(courses);
            courseTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        }
        if (btnEditSelected != null && btnDeleteSelected != null) {
            btnEditSelected.setDisable(true);
            btnDeleteSelected.setDisable(true);
            if (courseTable != null) {
                courseTable.getSelectionModel().selectedItemProperty().addListener((obs, oldV, newV) -> {
                    boolean disabled = newV == null;
                    btnEditSelected.setDisable(disabled);
                    btnDeleteSelected.setDisable(disabled);
                });
            }
        }
    }

    @FXML
    private void updateProgress(javafx.scene.input.KeyEvent evt) {
        updateProgress();
    }

    @FXML
    private void onAddCourse(ActionEvent e) {
        String name = courseName.getText().trim();
        String code = courseCode.getText().trim();
        String creditText = courseCredit.getText().trim();
        String t1 = teacher1.getText().trim();
        String t2 = teacher2.getText().trim();
        String gradeValue = grade.getValue();

       
        if (name.isEmpty() && code.isEmpty() && creditText.isEmpty() && t1.isEmpty() && t2.isEmpty() && gradeValue == null) {
            showAlert(Alert.AlertType.ERROR, "Please fill up the form.");
            return;
        }

        if (name.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Please enter Course Name.");
            return;
        }
        if (code.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Please enter Course Code.");
            return;
        }
        if (creditText.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Please enter Course Credit.");
            return;
        }

        double credit;
        try {
            credit = Double.parseDouble(creditText);
        } catch (NumberFormatException ex) {
            showAlert(Alert.AlertType.ERROR, "Invalid credit value. Use a number like 3 or 3.0");
            return;
        }

        if (gradeValue == null) {
            showAlert(Alert.AlertType.WARNING, "Please enter Grade.");
            return;
        }

        if (editingCourse != null) {
            editingCourse.setName(name);
            editingCourse.setCode(code);
            editingCourse.setCredit(credit);
            editingCourse.setTeacher1(t1);
            editingCourse.setTeacher2(t2);
            editingCourse.setGrade(gradeValue);
            if (courseTable != null) courseTable.refresh();
            showAlert(Alert.AlertType.INFORMATION, "Course updated successfully!");
            editingCourse = null;
            if (btnAddCourse != null) btnAddCourse.setText("Add Course");
        } else {
            Course c = new Course(name, code, credit, t1, t2, gradeValue);
            courses.add(c);
            showAlert(Alert.AlertType.INFORMATION, "Course added successfully!");
        }
        clearEntryFields();
        updateProgress();
    }

    @FXML
    private void onCalculate(ActionEvent e) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/gpa/result.fxml"));
        Parent root = loader.load();
        ResultController rc = loader.getController();
        rc.setCourses(courses);
        Navigation.setRoot(e, root);
    }

    private void updateProgress() {
        double totalReq = 0.0;
        if (!totalRequiredCredits.getText().trim().isEmpty()) {
            try {
                totalReq = Double.parseDouble(totalRequiredCredits.getText().trim());
            } catch (NumberFormatException ex) {
            }
        }

        double sum = courses.stream().mapToDouble(Course::getCredit).sum();
        lblProgress.setText(String.format("Credits entered: %.1f / %.1f", sum, totalReq));
        btnCalculate.setDisable(!(totalReq > 0 && Math.abs(sum - totalReq) < 0.0001));
    }

    public void setCourses(ObservableList<Course> incoming) {
        courses.setAll(incoming);
        if (courseTable != null) {
            courseTable.setItems(courses);
            courseTable.refresh();
        }
        updateProgress();
    }

    public void resetForRecalculate() {
        courses.clear();
        if (courseTable != null) {
            courseTable.setItems(courses);
            courseTable.getSelectionModel().clearSelection();
            courseTable.refresh();
        }
        clearEntryFields();
        if (totalRequiredCredits != null) totalRequiredCredits.clear();
        editingCourse = null;
        if (btnAddCourse != null) btnAddCourse.setText("Add Course");
        updateProgress();
        if (btnCalculate != null) btnCalculate.setDisable(true);
    }

    private void clearEntryFields() {
        courseName.clear();
        courseCode.clear();
        courseCredit.clear();
        teacher1.clear();
        teacher2.clear();
        grade.setValue(null);
    }

    private void showAlert(Alert.AlertType t, String msg) {
        Alert a = new Alert(t, msg, ButtonType.OK);
        a.showAndWait();
    }

    @FXML
    private void onReset(ActionEvent e) {
        courses.clear();
        clearEntryFields();
        totalRequiredCredits.clear();
        updateProgress();
        btnCalculate.setDisable(true);
        editingCourse = null;
        if (btnAddCourse != null) btnAddCourse.setText("Add Course");
        if (courseTable != null) courseTable.getSelectionModel().clearSelection();
    }

    @FXML
    private void onBack(ActionEvent e) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/gpa/home.fxml"));
        Parent root = loader.load();
        Navigation.setRoot(e, root);
    }

    @FXML
    private void onEditSelected(ActionEvent e) {
        if (courseTable == null) return;
        Course selected = courseTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Please select a course to edit.");
            return;
        }
        editingCourse = selected;
        courseName.setText(selected.getName());
        courseCode.setText(selected.getCode());
        courseCredit.setText(String.valueOf(selected.getCredit()));
        teacher1.setText(selected.getTeacher1());
        teacher2.setText(selected.getTeacher2());
        grade.setValue(selected.getGrade());
        if (btnAddCourse != null) btnAddCourse.setText("Update Course");
    }

    @FXML
    private void onDeleteSelected(ActionEvent e) {
        if (courseTable == null) return;
        Course selected = courseTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Please select a course to delete.");
            return;
        }
        courses.remove(selected);
        if (courseTable != null) courseTable.getSelectionModel().clearSelection();
        if (editingCourse == selected) {
            editingCourse = null;
            if (btnAddCourse != null) btnAddCourse.setText("Add Course");
            clearEntryFields();
        }
        updateProgress();
    }
}
