package com.db;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Database manager class for handling connections and operations with the H2 database
 */
public class DatabaseManager {
    private static final String DB_URL = "jdbc:h2:mem:hrdb;DB_CLOSE_DELAY=-1";
    private static final String DB_USER = "sa";
    private static final String DB_PASSWORD = "";
    
    private Connection connection;
    
    /**
     * Initialize the database connection and create the schema
     */
    public void init() throws SQLException {
        // Create connection
        connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        
        // Create tables
        createTables();
    }
    
    /**
     * Create the database schema
     */
    private void createTables() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            // Country table
            stmt.execute("CREATE TABLE IF NOT EXISTS countries (" +
                    "country_id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "country_name VARCHAR(100) NOT NULL)");
            
            // City table
            stmt.execute("CREATE TABLE IF NOT EXISTS cities (" +
                    "city_id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "city_name VARCHAR(100) NOT NULL, " +
                    "country_id INT, " +
                    "FOREIGN KEY (country_id) REFERENCES countries(country_id))");
            
            // Location table
            stmt.execute("CREATE TABLE IF NOT EXISTS locations (" +
                    "location_id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "street_address VARCHAR(255), " +
                    "postal_code VARCHAR(20), " +
                    "city_id INT, " +
                    "FOREIGN KEY (city_id) REFERENCES cities(city_id))");
            
            // Department table
            stmt.execute("CREATE TABLE IF NOT EXISTS departments (" +
                    "department_id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "department_name VARCHAR(100) NOT NULL, " +
                    "location_id INT, " +
                    "FOREIGN KEY (location_id) REFERENCES locations(location_id))");
            
            // Position/Job table
            stmt.execute("CREATE TABLE IF NOT EXISTS positions (" +
                    "position_id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "position_title VARCHAR(100) NOT NULL, " +
                    "min_salary DECIMAL(10, 2), " +
                    "max_salary DECIMAL(10, 2))");
            
            // Employee table
            stmt.execute("CREATE TABLE IF NOT EXISTS employees (" +
                    "employee_id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "first_name VARCHAR(50) NOT NULL, " +
                    "last_name VARCHAR(50) NOT NULL, " +
                    "email VARCHAR(100), " +
                    "phone_number VARCHAR(20), " +
                    "hire_date DATE, " +
                    "position_id INT, " +
                    "salary DECIMAL(10, 2), " +
                    "department_id INT, " +
                    "is_deleted BOOLEAN DEFAULT FALSE, " +
                    "FOREIGN KEY (position_id) REFERENCES positions(position_id), " +
                    "FOREIGN KEY (department_id) REFERENCES departments(department_id))");
            
            // Historical records table
            stmt.execute("CREATE TABLE IF NOT EXISTS employee_history (" +
                    "history_id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "employee_id INT, " +
                    "first_name VARCHAR(50) NOT NULL, " +
                    "last_name VARCHAR(50) NOT NULL, " +
                    "email VARCHAR(100), " +
                    "phone_number VARCHAR(20), " +
                    "hire_date DATE, " +
                    "termination_date DATE, " +
                    "position_id INT, " +
                    "salary DECIMAL(10, 2), " +
                    "department_id INT, " +
                    "FOREIGN KEY (employee_id) REFERENCES employees(employee_id))");
        }
    }
    
    /**
     * Execute an insert operation
     */
    public int executeInsert(String sql, Object... params) throws SQLException {
        try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            for (int i = 0; i < params.length; i++) {
                pstmt.setObject(i + 1, params[i]);
            }
            
            pstmt.executeUpdate();
            
            // Get the generated ID
            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return -1;
    }
    
    /**
     * Execute an update operation
     */
    public int executeUpdate(String sql, Object... params) throws SQLException {
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                pstmt.setObject(i + 1, params[i]);
            }
            
            return pstmt.executeUpdate();
        }
    }
    
    /**
     * Execute a select operation
     */
    public List<Map<String, Object>> executeQuery(String sql, Object... params) throws SQLException {
        List<Map<String, Object>> results = new ArrayList<>();
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                pstmt.setObject(i + 1, params[i]);
            }
            
            try (ResultSet rs = pstmt.executeQuery()) {
                ResultSetMetaData metaData = rs.getMetaData();
                int columnCount = metaData.getColumnCount();
                
                while (rs.next()) {
                    Map<String, Object> row = new HashMap<>();
                    
                    for (int i = 1; i <= columnCount; i++) {
                        String columnName = metaData.getColumnName(i);
                        Object value = rs.getObject(i);
                        row.put(columnName, value);
                    }
                    
                    results.add(row);
                }
            }
        }
        
        return results;
    }
    
    /**
     * Delete an employee (mark as deleted and add to history)
     */
    public boolean deleteEmployee(int employeeId) throws SQLException {
        connection.setAutoCommit(false);
        
        try {
            // Get employee data
            List<Map<String, Object>> employeeData = executeQuery(
                    "SELECT * FROM employees WHERE employee_id = ? AND is_deleted = FALSE", 
                    employeeId);
            
            if (employeeData.isEmpty()) {
                connection.rollback();
                return false;
            }
            
            Map<String, Object> employee = employeeData.get(0);
            
            // Add to history
            executeInsert(
                    "INSERT INTO employee_history (employee_id, first_name, last_name, email, phone_number, " +
                    "hire_date, termination_date, position_id, salary, department_id) " +
                    "VALUES (?, ?, ?, ?, ?, ?, CURRENT_DATE(), ?, ?, ?)",
                    employee.get("EMPLOYEE_ID"),
                    employee.get("FIRST_NAME"),
                    employee.get("LAST_NAME"),
                    employee.get("EMAIL"),
                    employee.get("PHONE_NUMBER"),
                    employee.get("HIRE_DATE"),
                    employee.get("POSITION_ID"),
                    employee.get("SALARY"),
                    employee.get("DEPARTMENT_ID")
            );
            
            // Mark as deleted
            executeUpdate(
                    "UPDATE employees SET is_deleted = TRUE WHERE employee_id = ?",
                    employeeId
            );
            
            connection.commit();
            return true;
        } catch (SQLException e) {
            connection.rollback();
            throw e;
        } finally {
            connection.setAutoCommit(true);
        }
    }
    
    /**
     * Close the database connection
     */
    public void close() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}