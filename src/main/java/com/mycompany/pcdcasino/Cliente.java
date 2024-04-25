package com.mycompany.pcdcasino;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Cliente {

    public static void main(String[] args) {
        try {
            // Establecer conexión con el servidor
            Socket socket = new Socket("127.0.0.1", 60270);
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            Scanner scanner = new Scanner(System.in);

            // Bucle para permitir al usuario realizar múltiples operaciones
            boolean continuar = true;
            while (continuar) {
                System.out.println("Introduzca su nombre: ");
                String nombre = scanner.nextLine();
                System.out.println("Introduzca su edad: ");
                int edad = Integer.parseInt(scanner.nextLine());
                System.out.println("Introduzca el saldo que desee depositar: ");
                int saldo = Integer.parseInt(scanner.nextLine());

                // Crear el objeto Persona
                Persona p = new Persona(edad, nombre, saldo);

                // Serializar y enviar el objeto Persona al servidor
                oos.writeObject(p);

                // Leer y mostrar la persona modificada recibida desde el servidor
                System.out.println("Esperando la modificación del objeto...");
                Persona p_modificada = (Persona) ois.readObject();
                System.out.println("Persona recibida desde el servidor: " + p_modificada);

                // Preguntar al usuario si desea realizar otra operación
                System.out.println("¿Desea realizar otra operación? (si/no)");
                String respuesta = scanner.nextLine();
                if (!respuesta.equalsIgnoreCase("si")) {
                    continuar = false; // Si la respuesta no es 'si', salir del bucle
                }
            }

            // Cerrar la conexión con el servidor
            socket.close();
        } catch (IOException e) {
            System.err.println("IOException. Mensaje: " + e.getMessage());
            e.printStackTrace(System.err);
            System.exit(1);
        } catch (ClassNotFoundException e) {
            System.err.println("ClassNotFoundException. Mensaje: " + e.getMessage());
            e.printStackTrace(System.err);
            System.exit(1);
        }

    }

}
