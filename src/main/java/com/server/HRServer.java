package com.server;

import com.db.DatabaseManager;
import com.model.*;
import com.protocol.Request;
import com.protocol.RequestType;
import com.protocol.Response;

import java.io.*;
import java.math.BigDecimal;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Socket server for handling HR database operations
 */
public class HRServer {
    private static final int PORT = 8888;
    private final DatabaseManager dbManager;
    private final ExecutorService threadPool;
    private ServerSocket serverSocket;
    private boolean running;
    
    public HRServer() {
        this.dbManager = new DatabaseManager();
        this.threadPool = Executors.newFixedThreadPool(10);
        this.running = false;
    }
    
    /**
     * Start the server
     */
    public void start() {
        try {
            // Initialize database
            dbManager.init();
            
            // Create server socket
            serverSocket = new ServerSocket(PORT);
            running = true;
            
            System.out.println("HR Server started on port " + PORT);
            
            // Accept client connections
            while (running) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected: " + clientSocket.getInetAddress().getHostAddress());
                
                // Handle client in a separate thread
                threadPool.execute(new ClientHandler(clientSocket));
            }
        } catch (IOException | SQLException e) {
            System.err.println("Server error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            stop();
        }
    }
    
    /**
     * Stop the server
     */
    public void stop() {
        running = false;
        threadPool.shutdown();
        
        if (serverSocket != null && !serverSocket.isClosed()) {
            try {
                serverSocket.close();
            } catch (IOException e) {
                System.err.println("Error closing server socket: " + e.getMessage());
            }
        }
        
        dbManager.close();
        System.out.println("Server stopped");
    }
    
    /**
     * Client handler class for processing client requests
     */
    private class ClientHandler implements Runnable {
        private final Socket clientSocket;
        
        public ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }
        
        @Override
        public void run() {
            try (
                ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
                ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream())
            ) {
                while (!clientSocket.isClosed()) {
                    // Read request from client
                    Request request = (Request) in.readObject();
                    System.out.println("Received request: " + request);
                    
                    // Process request and send response
                    Response response = processRequest(request);
                    out.writeObject(response);
                    out.flush();
                }
            } catch (EOFException e) {
                System.out.println("Client disconnected");
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Error handling client: " + e.getMessage());
            } finally {
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    System.err.println("Error closing client socket: " + e.getMessage());
                }
            }
        }
        
        /**
         * Process client request and return response
         */
        private Response processRequest(Request request) {
            try {
                switch (request.getType()) {
                    // INSERT operations
                    case INSERT_COUNTRY:
                        return insertCountry((Country) request.getData());
                    case INSERT_CITY:
                        return insertCity((City) request.getData());
                    case INSERT_LOCATION:
                        return insertLocation((Location) request.getData());
                    case INSERT_DEPARTMENT:
                        return insertDepartment((Department) request.getData());
                    case INSERT_POSITION:
                        return insertPosition((Position) request.getData());
                    case INSERT_EMPLOYEE:
                        return insertEmployee((Employee) request.getData());
                        
                    // UPDATE operations
                    case UPDATE_COUNTRY:
                        return updateCountry((Country) request.getData());
                    case UPDATE_CITY:
                        return updateCity((City) request.getData());
                    case UPDATE_LOCATION:
                        return updateLocation((Location) request.getData());
                    case UPDATE_DEPARTMENT:
                        return updateDepartment((Department) request.getData());
                    case UPDATE_POSITION:
                        return updatePosition((Position) request.getData());
                    case UPDATE_EMPLOYEE:
                        return updateEmployee((Employee) request.getData());
                        
                    // SELECT operations
                    case SELECT_COUNTRY:
                        return selectCountry((Integer) request.getData());
                    case SELECT_CITY:
                        return selectCity((Integer) request.getData());
                    case SELECT_LOCATION:
                        return selectLocation((Integer) request.getData());
                    case SELECT_DEPARTMENT:
                        return selectDepartment((Integer) request.getData());
                    case SELECT_POSITION:
                        return selectPosition((Integer) request.getData());
                    case SELECT_EMPLOYEE:
                        return selectEmployee((Integer) request.getData());
                        
                    // DELETE operations
                    case DELETE_EMPLOYEE:
                        return deleteEmployee((Integer) request.getData());
                        
                    default:
                        return Response.error("Unknown request type: " + request.getType());
                }
            } catch (Exception e) {
                e.printStackTrace();
                return Response.error("Error processing request: " + e.getMessage());
            }
        }
        
        // INSERT methods
        
        private Response insertCountry(Country country) throws SQLException {
            int id = dbManager.executeInsert(
                    "INSERT INTO countries (country_name) VALUES (?)",
                    country.getCountryName()
            );
            
            country.setCountryId(id);
            return Response.success("Country inserted successfully", country);
        }
        
        private Response insertCity(City city) throws SQLException {
            int id = dbManager.executeInsert(
                    "INSERT INTO cities (city_name, country_id) VALUES (?, ?)",
                    city.getCityName(), city.getCountryId()
            );
            
            city.setCityId(id);
            return Response.success("City inserted successfully", city);
        }
        
        private Response insertLocation(Location location) throws SQLException {
            int id = dbManager.executeInsert(
                    "INSERT INTO locations (street_address, postal_code, city_id) VALUES (?, ?, ?)",
                    location.getStreetAddress(), location.getPostalCode(), location.getCityId()
            );
            
            location.setLocationId(id);
            return Response.success("Location inserted successfully", location);
        }
        
        private Response insertDepartment(Department department) throws SQLException {
            int id = dbManager.executeInsert(
                    "INSERT INTO departments (department_name, location_id) VALUES (?, ?)",
                    department.getDepartmentName(), department.getLocationId()
            );
            
            department.setDepartmentId(id);
            return Response.success("Department inserted successfully", department);
        }
        
        private Response insertPosition(Position position) throws SQLException {
            int id = dbManager.executeInsert(
                    "INSERT INTO positions (position_title, min_salary, max_salary) VALUES (?, ?, ?)",
                    position.getPositionTitle(), position.getMinSalary(), position.getMaxSalary()
            );
            
            position.setPositionId(id);
            return Response.success("Position inserted successfully", position);
        }
        
        private Response insertEmployee(Employee employee) throws SQLException {
            int id = dbManager.executeInsert(
                    "INSERT INTO employees (first_name, last_name, email, phone_number, hire_date, " +
                    "position_id, salary, department_id, is_deleted) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)",
                    employee.getFirstName(), employee.getLastName(), employee.getEmail(),
                    employee.getPhoneNumber(), employee.getHireDate(), employee.getPositionId(),
                    employee.getSalary(), employee.getDepartmentId(), employee.isDeleted()
            );
            
            employee.setEmployeeId(id);
            return Response.success("Employee inserted successfully", employee);
        }
        
        // UPDATE methods
        
        private Response updateCountry(Country country) throws SQLException {
            int rowsAffected = dbManager.executeUpdate(
                    "UPDATE countries SET country_name = ? WHERE country_id = ?",
                    country.getCountryName(), country.getCountryId()
            );
            
            if (rowsAffected > 0) {
                return Response.success("Country updated successfully", country);
            } else {
                return Response.error("Country not found with ID: " + country.getCountryId());
            }
        }
        
        private Response updateCity(City city) throws SQLException {
            int rowsAffected = dbManager.executeUpdate(
                    "UPDATE cities SET city_name = ?, country_id = ? WHERE city_id = ?",
                    city.getCityName(), city.getCountryId(), city.getCityId()
            );
            
            if (rowsAffected > 0) {
                return Response.success("City updated successfully", city);
            } else {
                return Response.error("City not found with ID: " + city.getCityId());
            }
        }
        
        private Response updateLocation(Location location) throws SQLException {
            int rowsAffected = dbManager.executeUpdate(
                    "UPDATE locations SET street_address = ?, postal_code = ?, city_id = ? WHERE location_id = ?",
                    location.getStreetAddress(), location.getPostalCode(), location.getCityId(), location.getLocationId()
            );
            
            if (rowsAffected > 0) {
                return Response.success("Location updated successfully", location);
            } else {
                return Response.error("Location not found with ID: " + location.getLocationId());
            }
        }
        
        private Response updateDepartment(Department department) throws SQLException {
            int rowsAffected = dbManager.executeUpdate(
                    "UPDATE departments SET department_name = ?, location_id = ? WHERE department_id = ?",
                    department.getDepartmentName(), department.getLocationId(), department.getDepartmentId()
            );
            
            if (rowsAffected > 0) {
                return Response.success("Department updated successfully", department);
            } else {
                return Response.error("Department not found with ID: " + department.getDepartmentId());
            }
        }
        
        private Response updatePosition(Position position) throws SQLException {
            int rowsAffected = dbManager.executeUpdate(
                    "UPDATE positions SET position_title = ?, min_salary = ?, max_salary = ? WHERE position_id = ?",
                    position.getPositionTitle(), position.getMinSalary(), position.getMaxSalary(), position.getPositionId()
            );
            
            if (rowsAffected > 0) {
                return Response.success("Position updated successfully", position);
            } else {
                return Response.error("Position not found with ID: " + position.getPositionId());
            }
        }
        
        private Response updateEmployee(Employee employee) throws SQLException {
            int rowsAffected = dbManager.executeUpdate(
                    "UPDATE employees SET first_name = ?, last_name = ?, email = ?, phone_number = ?, " +
                    "hire_date = ?, position_id = ?, salary = ?, department_id = ?, is_deleted = ? " +
                    "WHERE employee_id = ?",
                    employee.getFirstName(), employee.getLastName(), employee.getEmail(),
                    employee.getPhoneNumber(), employee.getHireDate(), employee.getPositionId(),
                    employee.getSalary(), employee.getDepartmentId(), employee.isDeleted(),
                    employee.getEmployeeId()
            );
            
            if (rowsAffected > 0) {
                return Response.success("Employee updated successfully", employee);
            } else {
                return Response.error("Employee not found with ID: " + employee.getEmployeeId());
            }
        }
        
        // SELECT methods
        
        private Response selectCountry(Integer countryId) throws SQLException {
            List<Map<String, Object>> results = dbManager.executeQuery(
                    "SELECT * FROM countries WHERE country_id = ?",
                    countryId
            );
            
            if (!results.isEmpty()) {
                Map<String, Object> row = results.get(0);
                Country country = new Country(
                        (int) row.get("COUNTRY_ID"),
                        (String) row.get("COUNTRY_NAME")
                );
                return Response.success("Country found", country);
            } else {
                return Response.error("Country not found with ID: " + countryId);
            }
        }
        
        private Response selectCity(Integer cityId) throws SQLException {
            List<Map<String, Object>> results = dbManager.executeQuery(
                    "SELECT c.*, co.country_name FROM cities c " +
                    "JOIN countries co ON c.country_id = co.country_id " +
                    "WHERE c.city_id = ?",
                    cityId
            );
            
            if (!results.isEmpty()) {
                Map<String, Object> row = results.get(0);
                City city = new City(
                        (int) row.get("CITY_ID"),
                        (String) row.get("CITY_NAME"),
                        (int) row.get("COUNTRY_ID")
                );
                
                Country country = new Country(
                        (int) row.get("COUNTRY_ID"),
                        (String) row.get("COUNTRY_NAME")
                );
                
                city.setCountry(country);
                return Response.success("City found", city);
            } else {
                return Response.error("City not found with ID: " + cityId);
            }
        }
        
        private Response selectLocation(Integer locationId) throws SQLException {
            List<Map<String, Object>> results = dbManager.executeQuery(
                    "SELECT l.*, c.city_name, c.country_id, co.country_name FROM locations l " +
                    "JOIN cities c ON l.city_id = c.city_id " +
                    "JOIN countries co ON c.country_id = co.country_id " +
                    "WHERE l.location_id = ?",
                    locationId
            );
            
            if (!results.isEmpty()) {
                Map<String, Object> row = results.get(0);
                Location location = new Location(
                        (int) row.get("LOCATION_ID"),
                        (String) row.get("STREET_ADDRESS"),
                        (String) row.get("POSTAL_CODE"),
                        (int) row.get("CITY_ID")
                );
                
                City city = new City(
                        (int) row.get("CITY_ID"),
                        (String) row.get("CITY_NAME"),
                        (int) row.get("COUNTRY_ID")
                );
                
                Country country = new Country(
                        (int) row.get("COUNTRY_ID"),
                        (String) row.get("COUNTRY_NAME")
                );
                
                city.setCountry(country);
                location.setCity(city);
                
                return Response.success("Location found", location);
            } else {
                return Response.error("Location not found with ID: " + locationId);
            }
        }
        
        private Response selectDepartment(Integer departmentId) throws SQLException {
            List<Map<String, Object>> results = dbManager.executeQuery(
                    "SELECT d.*, l.street_address, l.postal_code, l.city_id FROM departments d " +
                    "JOIN locations l ON d.location_id = l.location_id " +
                    "WHERE d.department_id = ?",
                    departmentId
            );
            
            if (!results.isEmpty()) {
                Map<String, Object> row = results.get(0);
                Department department = new Department(
                        (int) row.get("DEPARTMENT_ID"),
                        (String) row.get("DEPARTMENT_NAME"),
                        (int) row.get("LOCATION_ID")
                );
                
                Location location = new Location(
                        (int) row.get("LOCATION_ID"),
                        (String) row.get("STREET_ADDRESS"),
                        (String) row.get("POSTAL_CODE"),
                        (int) row.get("CITY_ID")
                );
                
                department.setLocation(location);
                
                return Response.success("Department found", department);
            } else {
                return Response.error("Department not found with ID: " + departmentId);
            }
        }
        
        private Response selectPosition(Integer positionId) throws SQLException {
            List<Map<String, Object>> results = dbManager.executeQuery(
                    "SELECT * FROM positions WHERE position_id = ?",
                    positionId
            );
            
            if (!results.isEmpty()) {
                Map<String, Object> row = results.get(0);
                Position position = new Position(
                        (int) row.get("POSITION_ID"),
                        (String) row.get("POSITION_TITLE"),
                        (BigDecimal) row.get("MIN_SALARY"),
                        (BigDecimal) row.get("MAX_SALARY")
                );
                
                return Response.success("Position found", position);
            } else {
                return Response.error("Position not found with ID: " + positionId);
            }
        }
        
        private Response selectEmployee(Integer employeeId) throws SQLException {
            List<Map<String, Object>> results = dbManager.executeQuery(
                    "SELECT e.*, p.position_title, p.min_salary, p.max_salary, " +
                    "d.department_name, d.location_id FROM employees e " +
                    "JOIN positions p ON e.position_id = p.position_id " +
                    "JOIN departments d ON e.department_id = d.department_id " +
                    "WHERE e.employee_id = ? AND e.is_deleted = FALSE",
                    employeeId
            );
            
            if (!results.isEmpty()) {
                Map<String, Object> row = results.get(0);
                Employee employee = new Employee(
                        (int) row.get("EMPLOYEE_ID"),
                        (String) row.get("FIRST_NAME"),
                        (String) row.get("LAST_NAME"),
                        (String) row.get("EMAIL"),
                        (String) row.get("PHONE_NUMBER"),
                        (Date) row.get("HIRE_DATE"),
                        (int) row.get("POSITION_ID"),
                        (BigDecimal) row.get("SALARY"),
                        (int) row.get("DEPARTMENT_ID"),
                        (boolean) row.get("IS_DELETED")
                );
                
                Position position = new Position(
                        (int) row.get("POSITION_ID"),
                        (String) row.get("POSITION_TITLE"),
                        (BigDecimal) row.get("MIN_SALARY"),
                        (BigDecimal) row.get("MAX_SALARY")
                );
                
                Department department = new Department(
                        (int) row.get("DEPARTMENT_ID"),
                        (String) row.get("DEPARTMENT_NAME"),
                        (int) row.get("LOCATION_ID")
                );
                
                employee.setPosition(position);
                employee.setDepartment(department);
                
                return Response.success("Employee found", employee);
            } else {
                return Response.error("Employee not found with ID: " + employeeId);
            }
        }
        
        // DELETE methods
        
        private Response deleteEmployee(Integer employeeId) throws SQLException {
            boolean deleted = dbManager.deleteEmployee(employeeId);
            
            if (deleted) {
                return Response.success("Employee deleted successfully", employeeId);
            } else {
                return Response.error("Employee not found with ID: " + employeeId);
            }
        }
    }
    
    public static void main(String[] args) {
        HRServer server = new HRServer();
        server.start();
    }
}