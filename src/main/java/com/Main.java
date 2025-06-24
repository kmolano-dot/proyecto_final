package com;

import com.client.HRClient;
import com.server.HRServer;

import java.util.Scanner;

/**
 * Main class for the HR Management System
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("===== Sistema de Gestión de Recursos Humanos =====");
        System.out.println("1. Iniciar Servidor");
        System.out.println("2. Iniciar Cliente");
        System.out.print("Ingrese su opción: ");

        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();

        switch (choice) {
            case 1:
                startServer();
                break;
            case 2:
                startClient();
                break;
            default:
                System.out.println("Opción inválida. Saliendo...");
        }

        scanner.close();
    }

    /**
     * Start the HR server
     */
    private static void startServer() {
        System.out.println("Iniciando Servidor de RRHH...");
        HRServer server = new HRServer();
        server.start();
    }

    /**
     * Start the HR client
     */
    private static void startClient() {
        System.out.println("Iniciando Cliente de RRHH...");
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
