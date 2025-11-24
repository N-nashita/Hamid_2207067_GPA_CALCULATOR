package com.example.gpa.db;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {
    private static final String DB_NAME = "gpa_calculator.db";

    private static final Path DB_PATH = Paths.get(System.getProperty("user.dir"), "src", "main", "java", "com", "example", "gpa", DB_NAME);
    
    private static final Path LEGACY_HOME_DB_PATH = Paths.get(System.getProperty("user.home"), DB_NAME);
    private static final Path LEGACY_DATA_DB_PATH = Paths.get(System.getProperty("user.dir"), "data", DB_NAME);
    private static final String JDBC_URL = "jdbc:sqlite:" + DB_PATH;

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL);
    }

    public static void init() throws SQLException {
        ensureDirectoryAndMigrate();
        try (Connection conn = getConnection(); Statement st = conn.createStatement()) {
                st.executeUpdate("CREATE TABLE IF NOT EXISTS courses (" +
                    "code TEXT PRIMARY KEY," +
                    "name TEXT NOT NULL," +
                    "credit REAL NOT NULL," +
                    "teacher1 TEXT," +
                    "teacher2 TEXT," +
                    "grade TEXT NOT NULL" +
                    ")");
        }
    }

    public static void dropGpaTotals() {
        try (Connection conn = getConnection(); Statement st = conn.createStatement()) {
            st.executeUpdate("DROP TABLE IF EXISTS gpa_totals");
        } catch (SQLException ignored) {}
    }

    private static void ensureDirectoryAndMigrate() {
        try {
            Path dir = DB_PATH.getParent();
            if (dir != null && Files.notExists(dir)) {
                Files.createDirectories(dir);
            }
            if (Files.notExists(DB_PATH)) {
                if (Files.exists(LEGACY_DATA_DB_PATH)) {
                    Files.copy(LEGACY_DATA_DB_PATH, DB_PATH);
                } else if (Files.exists(LEGACY_HOME_DB_PATH)) {
                    Files.copy(LEGACY_HOME_DB_PATH, DB_PATH);
                }
            }
        } catch (IOException e) {
        }
    }
}
