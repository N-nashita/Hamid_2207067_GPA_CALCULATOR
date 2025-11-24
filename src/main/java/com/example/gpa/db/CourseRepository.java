package com.example.gpa.db;

import com.example.gpa.model.Course;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class CourseRepository {

    public void init() throws SQLException {
        Database.init();
    }

    public ObservableList<Course> findAll() throws SQLException {
        ObservableList<Course> list = FXCollections.observableArrayList();
        String sql = "SELECT code,name,credit,teacher1,teacher2,grade FROM courses ORDER BY name";
        try (Connection c = Database.getConnection(); PreparedStatement ps = c.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Course course = new Course(
                        rs.getString("name"),
                        rs.getString("code"),
                        rs.getDouble("credit"),
                        rs.getString("teacher1"),
                        rs.getString("teacher2"),
                        rs.getString("grade")
                );
                list.add(course);
            }
        }
        return list;
    }

    public boolean insert(Course course) throws SQLException {
        String sql = "INSERT INTO courses(code,name,credit,teacher1,teacher2,grade) VALUES(?,?,?,?,?,?)";
        try (Connection c = Database.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, course.getCode());
            ps.setString(2, course.getName());
            ps.setDouble(3, course.getCredit());
            ps.setString(4, course.getTeacher1());
            ps.setString(5, course.getTeacher2());
            ps.setString(6, course.getGrade());
            return ps.executeUpdate() == 1;
        }
    }

    public boolean updateByOriginalCode(String originalCode, Course updated) throws SQLException {
        String sql = "UPDATE courses SET code=?, name=?, credit=?, teacher1=?, teacher2=?, grade=? WHERE code=?";
        try (Connection c = Database.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, updated.getCode());
            ps.setString(2, updated.getName());
            ps.setDouble(3, updated.getCredit());
            ps.setString(4, updated.getTeacher1());
            ps.setString(5, updated.getTeacher2());
            ps.setString(6, updated.getGrade());
            ps.setString(7, originalCode);
            return ps.executeUpdate() == 1;
        }
    }

    public boolean deleteByCode(String code) throws SQLException {
        String sql = "DELETE FROM courses WHERE code=?";
        try (Connection c = Database.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, code);
            return ps.executeUpdate() == 1;
        }
    }
}
