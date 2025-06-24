package com.client;

import com.model.*;
import com.protocol.Request;
import com.protocol.RequestType;
import com.protocol.Response;

import java.io.*;
import java.math.BigDecimal;
import java.net.Socket;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Scanner;

/**
 * Socket client for interacting with the HR server
 */
public class HRClient {
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 8888;

    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private Scanner scanner;

    /**
     * Connect to the server
     */
    public boolean connect() {
        try {
            socket = new Socket(SERVER_HOST, SERVER_PORT);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            scanner = new Scanner(System.in);

            System.out.println("Conectado al servidor de RRHH en " + SERVER_HOST + ":" + SERVER_PORT);
            return true;
        } catch (IOException e) {
            System.err.println("Error al conectar con el servidor: " + e.getMessage());
            return false;
        }
    }

    /**
     * Close the connection
     */
    public void close() {
        try {
            if (in != null) in.close();
            if (out != null) out.close();
            if (socket != null) socket.close();
            if (scanner != null) scanner.close();
        } catch (IOException e) {
            System.err.println("Error al cerrar la conexión: " + e.getMessage());
        }
    }

    /**
     * Send a request to the server and get the response
     */
    private Response sendRequest(Request request) {
        try {
            out.writeObject(request);
            out.flush();
            return (Response) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error al enviar la solicitud: " + e.getMessage());
            return Response.error("Error de comunicación: " + e.getMessage());
        }
    }

    /**
     * Display the main menu and handle user input
     */
    public void start() {
        boolean running = true;

        while (running) {
            System.out.println("\n===== Sistema de Gestión de RRHH =====");
            System.out.println("1. Operaciones de inserción");
            System.out.println("2. Operaciones de actualización");
            System.out.println("3. Operaciones de consulta");
            System.out.println("4. Eliminar empleado");
            System.out.println("0. Salir");
            System.out.print("Ingrese su opción: ");

            int choice = readInt();

            switch (choice) {
                case 0:
                    running = false;
                    break;
                case 1:
                    handleInsertOperations();
                    break;
                case 2:
                    handleUpdateOperations();
                    break;
                case 3:
                    handleSelectOperations();
                    break;
                case 4:
                    handleDeleteEmployee();
                    break;
                default:
                    System.out.println("Opción inválida. Por favor, intente de nuevo.");
            }
        }
    }

    /**
     * Handle insert operations menu
     */
    private void handleInsertOperations() {
        System.out.println("\n===== Operaciones de Inserción =====");
        System.out.println("1. Insertar País");
        System.out.println("2. Insertar Ciudad");
        System.out.println("3. Insertar Ubicación");
        System.out.println("4. Insertar Departamento");
        System.out.println("5. Insertar Cargo");
        System.out.println("6. Insertar Empleado");
        System.out.println("0. Volver al menú principal");
        System.out.print("Ingrese su opción: ");

        int choice = readInt();

        switch (choice) {
            case 0:
                return;
            case 1:
                insertCountry();
                break;
            case 2:
                insertCity();
                break;
            case 3:
                insertLocation();
                break;
            case 4:
                insertDepartment();
                break;
            case 5:
                insertPosition();
                break;
            case 6:
                insertEmployee();
                break;
            default:
                System.out.println("Opción inválida. Por favor, intente de nuevo.");
        }
    }

    /**
     * Handle update operations menu
     */
    private void handleUpdateOperations() {
        System.out.println("\n===== Operaciones de Actualización =====");
        System.out.println("1. Actualizar País");
        System.out.println("2. Actualizar Ciudad");
        System.out.println("3. Actualizar Ubicación");
        System.out.println("4. Actualizar Departamento");
        System.out.println("5. Actualizar Cargo");
        System.out.println("6. Actualizar Empleado");
        System.out.println("0. Volver al menú principal");
        System.out.print("Ingrese su opción: ");

        int choice = readInt();

        switch (choice) {
            case 0:
                return;
            case 1:
                updateCountry();
                break;
            case 2:
                updateCity();
                break;
            case 3:
                updateLocation();
                break;
            case 4:
                updateDepartment();
                break;
            case 5:
                updatePosition();
                break;
            case 6:
                updateEmployee();
                break;
            default:
                System.out.println("Opción inválida. Por favor, intente de nuevo.");
        }
    }

    /**
     * Handle select operations menu
     */
    private void handleSelectOperations() {
        System.out.println("\n===== Operaciones de Consulta =====");
        System.out.println("1. Consultar País");
        System.out.println("2. Consultar Ciudad");
        System.out.println("3. Consultar Ubicación");
        System.out.println("4. Consultar Departamento");
        System.out.println("5. Consultar Cargo");
        System.out.println("6. Consultar Empleado");
        System.out.println("0. Volver al menú principal");
        System.out.print("Ingrese su opción: ");

        int choice = readInt();

        switch (choice) {
            case 0:
                return;
            case 1:
                selectCountry();
                break;
            case 2:
                selectCity();
                break;
            case 3:
                selectLocation();
                break;
            case 4:
                selectDepartment();
                break;
            case 5:
                selectPosition();
                break;
            case 6:
                selectEmployee();
                break;
            default:
                System.out.println("Opción inválida. Por favor, intente de nuevo.");
        }
    }

    // INSERT methods

    private void insertCountry() {
        System.out.println("\n===== Insertar País =====");
        System.out.print("Ingrese nombre del país: ");
        String countryName = scanner.nextLine();

        Country country = new Country(countryName);
        Request request = new Request(RequestType.INSERT_COUNTRY, country);
        Response response = sendRequest(request);

        displayResponse(response);
    }

    private void insertCity() {
        System.out.println("\n===== Insertar Ciudad =====");
        System.out.print("Ingrese nombre de la ciudad: ");
        String cityName = scanner.nextLine();

        System.out.print("Ingrese ID del país: ");
        int countryId = readInt();

        City city = new City(cityName, countryId);
        Request request = new Request(RequestType.INSERT_CITY, city);
        Response response = sendRequest(request);

        displayResponse(response);
    }

    private void insertLocation() {
        System.out.println("\n===== Insertar Ubicación =====");
        System.out.print("Ingrese dirección: ");
        String streetAddress = scanner.nextLine();

        System.out.print("Ingrese código postal: ");
        String postalCode = scanner.nextLine();

        System.out.print("Ingrese ID de la ciudad: ");
        int cityId = readInt();

        Location location = new Location(streetAddress, postalCode, cityId);
        Request request = new Request(RequestType.INSERT_LOCATION, location);
        Response response = sendRequest(request);

        displayResponse(response);
    }

    private void insertDepartment() {
        System.out.println("\n===== Insertar Departamento =====");
        System.out.print("Ingrese nombre del departamento: ");
        String departmentName = scanner.nextLine();

        System.out.print("Ingrese ID de la ubicación: ");
        int locationId = readInt();

        Department department = new Department(departmentName, locationId);
        Request request = new Request(RequestType.INSERT_DEPARTMENT, department);
        Response response = sendRequest(request);

        displayResponse(response);
    }

    private void insertPosition() {
        System.out.println("\n===== Insertar Cargo =====");
        System.out.print("Ingrese título del cargo: ");
        String positionTitle = scanner.nextLine();

        System.out.print("Ingrese salario mínimo: ");
        BigDecimal minSalary = readBigDecimal();

        System.out.print("Ingrese salario máximo: ");
        BigDecimal maxSalary = readBigDecimal();

        Position position = new Position(positionTitle, minSalary, maxSalary);
        Request request = new Request(RequestType.INSERT_POSITION, position);
        Response response = sendRequest(request);

        displayResponse(response);
    }

    private void insertEmployee() {
        System.out.println("\n===== Insertar Empleado =====");
        System.out.print("Ingrese nombre: ");
        String firstName = scanner.nextLine();

        System.out.print("Ingrese apellido: ");
        String lastName = scanner.nextLine();

        System.out.print("Ingrese correo electrónico: ");
        String email = scanner.nextLine();

        System.out.print("Ingrese número de teléfono: ");
        String phoneNumber = scanner.nextLine();

        System.out.print("Ingrese fecha de contratación (yyyy-MM-dd): ");
        Date hireDate = readDate();

        System.out.print("Ingrese ID del cargo: ");
        int positionId = readInt();

        System.out.print("Ingrese salario: ");
        BigDecimal salary = readBigDecimal();

        System.out.print("Ingrese ID del departamento: ");
        int departmentId = readInt();

        Employee employee = new Employee(firstName, lastName, email, phoneNumber, 
                                        hireDate, positionId, salary, departmentId);

        Request request = new Request(RequestType.INSERT_EMPLOYEE, employee);
        Response response = sendRequest(request);

        displayResponse(response);
    }

    // UPDATE methods

    private void updateCountry() {
        System.out.println("\n===== Actualizar País =====");
        System.out.print("Ingrese ID del país: ");
        int countryId = readInt();

        System.out.print("Ingrese nuevo nombre del país: ");
        String countryName = scanner.nextLine();

        Country country = new Country(countryId, countryName);
        Request request = new Request(RequestType.UPDATE_COUNTRY, country);
        Response response = sendRequest(request);

        displayResponse(response);
    }

    private void updateCity() {
        System.out.println("\n===== Actualizar Ciudad =====");
        System.out.print("Ingrese ID de la ciudad: ");
        int cityId = readInt();

        System.out.print("Ingrese nuevo nombre de la ciudad: ");
        String cityName = scanner.nextLine();

        System.out.print("Ingrese nuevo ID del país: ");
        int countryId = readInt();

        City city = new City(cityId, cityName, countryId);
        Request request = new Request(RequestType.UPDATE_CITY, city);
        Response response = sendRequest(request);

        displayResponse(response);
    }

    private void updateLocation() {
        System.out.println("\n===== Actualizar Ubicación =====");
        System.out.print("Ingrese ID de la ubicación: ");
        int locationId = readInt();

        System.out.print("Ingrese nueva dirección: ");
        String streetAddress = scanner.nextLine();

        System.out.print("Ingrese nuevo código postal: ");
        String postalCode = scanner.nextLine();

        System.out.print("Ingrese nuevo ID de la ciudad: ");
        int cityId = readInt();

        Location location = new Location(locationId, streetAddress, postalCode, cityId);
        Request request = new Request(RequestType.UPDATE_LOCATION, location);
        Response response = sendRequest(request);

        displayResponse(response);
    }

    private void updateDepartment() {
        System.out.println("\n===== Actualizar Departamento =====");
        System.out.print("Ingrese ID del departamento: ");
        int departmentId = readInt();

        System.out.print("Ingrese nuevo nombre del departamento: ");
        String departmentName = scanner.nextLine();

        System.out.print("Ingrese nuevo ID de la ubicación: ");
        int locationId = readInt();

        Department department = new Department(departmentId, departmentName, locationId);
        Request request = new Request(RequestType.UPDATE_DEPARTMENT, department);
        Response response = sendRequest(request);

        displayResponse(response);
    }

    private void updatePosition() {
        System.out.println("\n===== Actualizar Cargo =====");
        System.out.print("Ingrese ID del cargo: ");
        int positionId = readInt();

        System.out.print("Ingrese nuevo título del cargo: ");
        String positionTitle = scanner.nextLine();

        System.out.print("Ingrese nuevo salario mínimo: ");
        BigDecimal minSalary = readBigDecimal();

        System.out.print("Ingrese nuevo salario máximo: ");
        BigDecimal maxSalary = readBigDecimal();

        Position position = new Position(positionId, positionTitle, minSalary, maxSalary);
        Request request = new Request(RequestType.UPDATE_POSITION, position);
        Response response = sendRequest(request);

        displayResponse(response);
    }

    private void updateEmployee() {
        System.out.println("\n===== Actualizar Empleado =====");
        System.out.print("Ingrese ID del empleado: ");
        int employeeId = readInt();

        System.out.print("Ingrese nuevo nombre: ");
        String firstName = scanner.nextLine();

        System.out.print("Ingrese nuevo apellido: ");
        String lastName = scanner.nextLine();

        System.out.print("Ingrese nuevo correo electrónico: ");
        String email = scanner.nextLine();

        System.out.print("Ingrese nuevo número de teléfono: ");
        String phoneNumber = scanner.nextLine();

        System.out.print("Ingrese nueva fecha de contratación (yyyy-MM-dd): ");
        Date hireDate = readDate();

        System.out.print("Ingrese nuevo ID del cargo: ");
        int positionId = readInt();

        System.out.print("Ingrese nuevo salario: ");
        BigDecimal salary = readBigDecimal();

        System.out.print("Ingrese nuevo ID del departamento: ");
        int departmentId = readInt();

        Employee employee = new Employee(employeeId, firstName, lastName, email, phoneNumber, 
                                        hireDate, positionId, salary, departmentId, false);

        Request request = new Request(RequestType.UPDATE_EMPLOYEE, employee);
        Response response = sendRequest(request);

        displayResponse(response);
    }

    // SELECT methods

    private void selectCountry() {
        System.out.println("\n===== Consultar País =====");
        System.out.print("Ingrese ID del país: ");
        int countryId = readInt();

        Request request = new Request(RequestType.SELECT_COUNTRY, countryId);
        Response response = sendRequest(request);

        displayResponse(response);
    }

    private void selectCity() {
        System.out.println("\n===== Consultar Ciudad =====");
        System.out.print("Ingrese ID de la ciudad: ");
        int cityId = readInt();

        Request request = new Request(RequestType.SELECT_CITY, cityId);
        Response response = sendRequest(request);

        displayResponse(response);
    }

    private void selectLocation() {
        System.out.println("\n===== Consultar Ubicación =====");
        System.out.print("Ingrese ID de la ubicación: ");
        int locationId = readInt();

        Request request = new Request(RequestType.SELECT_LOCATION, locationId);
        Response response = sendRequest(request);

        displayResponse(response);
    }

    private void selectDepartment() {
        System.out.println("\n===== Consultar Departamento =====");
        System.out.print("Ingrese ID del departamento: ");
        int departmentId = readInt();

        Request request = new Request(RequestType.SELECT_DEPARTMENT, departmentId);
        Response response = sendRequest(request);

        displayResponse(response);
    }

    private void selectPosition() {
        System.out.println("\n===== Consultar Cargo =====");
        System.out.print("Ingrese ID del cargo: ");
        int positionId = readInt();

        Request request = new Request(RequestType.SELECT_POSITION, positionId);
        Response response = sendRequest(request);

        displayResponse(response);
    }

    private void selectEmployee() {
        System.out.println("\n===== Consultar Empleado =====");
        System.out.print("Ingrese ID del empleado: ");
        int employeeId = readInt();

        Request request = new Request(RequestType.SELECT_EMPLOYEE, employeeId);
        Response response = sendRequest(request);

        displayResponse(response);
    }

    // DELETE methods

    private void handleDeleteEmployee() {
        System.out.println("\n===== Eliminar Empleado =====");
        System.out.print("Ingrese ID del empleado: ");
        int employeeId = readInt();

        Request request = new Request(RequestType.DELETE_EMPLOYEE, employeeId);
        Response response = sendRequest(request);

        displayResponse(response);
    }

    // Utility methods

    private int readInt() {
        while (true) {
            try {
                String input = scanner.nextLine();
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.print("Número inválido. Por favor, intente de nuevo: ");
            }
        }
    }

    private BigDecimal readBigDecimal() {
        while (true) {
            try {
                String input = scanner.nextLine();
                return new BigDecimal(input);
            } catch (NumberFormatException e) {
                System.out.print("Número inválido. Por favor, intente de nuevo: ");
            }
        }
    }

    private Date readDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);

        while (true) {
            try {
                String input = scanner.nextLine();
                java.util.Date utilDate = dateFormat.parse(input);
                return new Date(utilDate.getTime());
            } catch (ParseException e) {
                System.out.print("Formato de fecha inválido. Por favor, use yyyy-MM-dd: ");
            }
        }
    }

    private void displayResponse(Response response) {
        System.out.println("\n===== Respuesta del Servidor =====");
        System.out.println("Estado: " + (response.isSuccess() ? "Éxito" : "Error"));
        System.out.println("Mensaje: " + response.getMessage());

        if (response.isSuccess() && response.getData() != null) {
            System.out.println("Datos: " + response.getData());
        }
    }

    public static void main(String[] args) {
        HRClient client = new HRClient();

        if (client.connect()) {
            try {
                client.start();
            } finally {
                client.close();
            }
        }
    }
}
