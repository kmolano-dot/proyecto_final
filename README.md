# HR Management System

This is a client-server application for managing a Human Resources database. The application uses sockets for communication between the client and server, and an H2 in-memory database for storing the data.

## Features

- CRUD operations for HR entities (Country, City, Location, Department, Position, Employee)
- Socket-based client-server architecture
- In-memory H2 database
- Transaction support for employee deletion with historical records

## Requirements

- Java 21 or higher
- Maven

## How to Run

1. Compile the project:
   ```
   mvn clean package
   ```

2. Run the application:
   ```
   java -cp target/proyecto_final-1.0-SNAPSHOT.jar com.Main
   ```

3. Choose whether to start the server or the client:
   - Option 1: Start the server
   - Option 2: Start the client

Note: You need to start the server before starting the client.

## Usage

### Server

The server runs on port 8888 and handles client requests for database operations. It initializes the H2 database and creates the necessary tables.

### Client

The client connects to the server and provides a command-line interface for performing operations on the HR database.

#### Main Menu

1. Insert operations
2. Update operations
3. Select operations
4. Delete employee
0. Exit

#### Insert Operations

1. Insert Country
2. Insert City
3. Insert Location
4. Insert Department
5. Insert Position
6. Insert Employee
0. Back to main menu

#### Update Operations

1. Update Country
2. Update City
3. Update Location
4. Update Department
5. Update Position
6. Update Employee
0. Back to main menu

#### Select Operations

1. Select Country
2. Select City
3. Select Location
4. Select Department
5. Select Position
6. Select Employee
0. Back to main menu

## Example Workflow

1. Start the server
2. Start the client
3. Create a country, city, location, department, position, and employee:
   - Insert a country (e.g., "Colombia")
   - Insert a city (e.g., "Bogotá") with the country ID
   - Insert a location (e.g., "Calle 100 #15-20", "110111") with the city ID
   - Insert a department (e.g., "Human Resources") with the location ID
   - Insert a position (e.g., "HR Manager", 5000, 10000)
   - Insert an employee with the position ID and department ID
4. Query employee information using the Select Employee option
5. Delete an employee using the Delete Employee option (this will mark the employee as deleted and add them to the historical records)

## Database Schema

- **countries**: country_id, country_name
- **cities**: city_id, city_name, country_id
- **locations**: location_id, street_address, postal_code, city_id
- **departments**: department_id, department_name, location_id
- **positions**: position_id, position_title, min_salary, max_salary
- **employees**: employee_id, first_name, last_name, email, phone_number, hire_date, position_id, salary, department_id, is_deleted
- **employee_history**: history_id, employee_id, first_name, last_name, email, phone_number, hire_date, termination_date, position_id, salary, department_id

## Architecture

- **Client**: Connects to the server, sends requests, and displays responses
- **Server**: Accepts client connections, processes requests, and executes database operations
- **Protocol**: Defines the communication protocol between client and server
- **Database Manager**: Handles database connections and operations
- **Model Classes**: Define the entities in the HR system