package com.mycompany.pcdcasino;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

public class PCDCasino {

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(60270);
            System.out.println("Esperando a un cliente");
            Socket cliente = serverSocket.accept();
            ObjectInputStream ois = new ObjectInputStream(cliente.getInputStream());
            ObjectOutputStream oos = new ObjectOutputStream(cliente.getOutputStream());

            Persona p = (Persona) ois.readObject();

            int seleccion = 0;
            while (seleccion != 4) {
                System.out.println("*************************************************");
                System.out.println("*               ¡¡ Bienvenido al Casino !!      *");
                System.out.println("*-----------------------------------------------*");
                System.out.println("*          Seleccione uno de nuestros juegos:   *");
                System.out.println("*                                               *");
                System.out.println("*            1. Ruleta de la Fortuna            *");
                System.out.println("*            2. Tragaperras Western             *");
                System.out.println("*            3. Consultar saldo                 *");
                System.out.println("*            4. Salir                           *");
                System.out.println("*                                               *");
                System.out.println("*************************************************");
                System.out.println("Seleccione una opción: ");
                seleccion = (int) ois.readObject();
                switch (seleccion) {
                    case 1:
                        System.out.println("Juego seleccionado: Ruleta de la Fortuna");
                        jugarRuleta(p);
                        consultarSaldo(p, oos);
                        break;
                    case 2:
                        System.out.println("Juego seleccionado: Tragaperras Western");
                        jugarTragaperras(p, oos);
                        consultarSaldo(p, oos);
                        break;
                    case 3:
                        consultarSaldo(p, oos);
                        break;
                    case 4:
                        System.out.println("Saliendo del casino...");
                        break;
                    default:
                        System.out.println("Opción no válida. Por favor, seleccione nuevamente.");
                        break;
                }
            }

            serverSocket.close();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }

    }

    private static void consultarSaldo(Persona p, ObjectOutputStream oos) {
        try {
            oos.writeObject(p);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void jugarRuleta(Persona p) {
        Random random = new Random();
        int resultado = random.nextInt(37); // Números del 0 al 36
        System.out.println("La ruleta ha caído en el número: " + resultado);

        int nuevoSaldo = p.getSaldo() - 10; // Supongamos que jugar cuesta 10 unidades
        p.setSaldo(nuevoSaldo);
    }

    private static void jugarTragaperras(Persona p, ObjectOutputStream oos) {
        // Símbolos de las frutas
        String[] frutas = {"🍋", "🍏", "🍐", "🥝", "🍌"};

        // Simular una tirada de tragaperras
        Random random = new Random();
        String resultado = frutas[random.nextInt(frutas.length)] + " | "
                        + frutas[random.nextInt(frutas.length)] + " | "
                        + frutas[random.nextInt(frutas.length)];

        // Actualizar el saldo del jugador después de jugar
        int nuevoSaldo = p.getSaldo() - 5; // Supongamos que jugar cuesta 5 unidades
        p.setSaldo(nuevoSaldo);

        try {
            // Enviar el resultado al cliente
            oos.writeObject(resultado);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
