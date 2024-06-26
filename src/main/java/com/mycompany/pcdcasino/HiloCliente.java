/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.pcdcasino;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Random;
import java.util.Scanner;

/**
 *
 * @author dlmja
 */
public class HiloCliente implements Runnable {

    private Socket cliente;

    public HiloCliente(Socket cliente) {
        this.cliente = cliente;
    }

    @Override
    public void run() {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(cliente.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(cliente.getInputStream());

            int seleccion = 0;

            while (seleccion != 5) {
                System.out.println("*************************************************");
                System.out.println("*               ¡¡ Bienvenido al Casino !!      *");
                System.out.println("*-----------------------------------------------*");
                System.out.println("*          Seleccione uno de nuestros juegos:   *");
                System.out.println("*                                               *");
                System.out.println("*            1. Ruleta de la Fortuna            *");
                System.out.println("*            2. Tragaperras Western             *");
                System.out.println("*            3. Consultar saldo                 *");
                System.out.println("*            4. Cartas del Tarot                *");
                System.out.println("*            5. Salir                           *");
                System.out.println("*                                               *");
                System.out.println("*************************************************");
                
                Persona p = (Persona) ois.readObject();
                System.out.println("Seleccione una opción: "+ p.getNombre() );
                Scanner scanner = new Scanner(System.in);
                seleccion = scanner.nextInt();
                switch (seleccion) {
                    case 1:
                        System.out.println("Juego seleccionado: Ruleta de la Fortuna");
                        jugarRuleta(p);
                        consultarSaldo(p);
                        oos.writeObject(p);
                        break;
                    case 2:
                        System.out.println("Juego seleccionado: Tragaperras Western");
                        jugarTragaperras(p);
                        consultarSaldo(p);
                        oos.writeObject(p);
                        break;
                    case 3:
                        consultarSaldo(p);
                        oos.writeObject(p);
                        break;
                    case 4:
                        cartasTarot(p);
                        consultarSaldo(p);
                        oos.writeObject(p);
                        break;
                    case 5:
                        System.out.println("Saliendo del casino...");
                        break;
                    default:
                        System.out.println("Opción no válida. Por favor, seleccione nuevamente.");
                        break;
                }
            }
            cliente.close();

        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static void jugarRuleta(Persona p) {
        //Hacer apuesta
        Scanner scanner = new Scanner(System.in);
        System.out.println("Introduce la cantidad de fichas que quiere apostar a ese numero (si no quiere apostar a un numero ingrese 0)");
        int apuestaNumero = scanner.nextInt();
        System.out.println("Introduce el numero al que quiere apostar: ");
        int numeroElegido;
        if (apuestaNumero != 0) {
            System.out.println("Introduce el numero al que quiere apostar: ");
            numeroElegido = scanner.nextInt();
        } else {
            numeroElegido = -1;
        }

        System.out.println("Introduce la cantidad de fichas que quiere apostar a ese color (si no quiere apostar a un numero ingrese 0)");
        int apuestaColor = scanner.nextInt();
        int color;
        if (apuestaColor != 0) {
            System.out.println("Introduce el color al que quieres apostar:\n1.Rojo\n2.Negro ");
            color = scanner.nextInt();
        } else {
            color = 0;
        }

        System.out.println("Introduce la cantidad de fichas que quiere apostar a esa docena (si no quiere apostar a un numero ingrese 0)");
        int apuestaDocena = scanner.nextInt();
        int docena;
        if (apuestaDocena != 0) {
            System.out.println("Introduce la docena a la que quieres apostar:\n1.Primera docena\n2.Segunda docena\n3.Tercera docena ");
            docena = scanner.nextInt();
        } else {
            docena = 0;
        }

        int nuevoSaldo = p.getSaldo() - (apuestaNumero + apuestaColor + apuestaDocena); // Supongamos que jugar cuesta 10 unidades
        p.setSaldo(nuevoSaldo);

        //Obtener resultado (numero, color y docena)
        int[] rojos = {1, 3, 5, 7, 9, 12, 14, 16, 18, 19, 21, 23, 25, 27, 30, 32, 34, 36};
        int[] negros = {2, 4, 6, 8, 10, 11, 13, 15, 17, 20, 22, 24, 26, 28, 29, 31, 33, 35};

        Random random = new Random();
        int numero = random.nextInt(37); // Números del 0 al 36

        String colorResultado = null;
        String docenaResultado = null;

        for (int i = 0; i < rojos.length; i++) {
            if (rojos[i] == numero) {
                colorResultado = "Rojo";
                break;
            }
            if (negros[i] == numero) {
                colorResultado = "Negro";
                break;
            } else {
                colorResultado = "Verde";
            }
            if (0 < numero && numero < 13) {
                docenaResultado = "Primera docena";
            }
            if (12 < numero && numero < 25) {
                docenaResultado = "Segunda docena";
            }
            if (24 < numero && numero < 37) {
                docenaResultado = "Tercera docena";
            }
        }
        NumeroRule resultado = new NumeroRule(numero, colorResultado, docenaResultado);
        System.out.println("La ruleta ha caído en el número: " + numero + ", " + colorResultado + ", " + docenaResultado);

        //Calcular el saldo tras la tirada
        int ganancias = 0;
        if (numeroElegido == resultado.getNumero()) {
            ganancias += apuestaNumero * 36;
        }

        if (color == 1) {
            if ("Rojo".equals(resultado.getColor())) {
                ganancias += apuestaColor * 2;
            }
        }
        if (color == 2) {
            if ("Negro".equals(resultado.getColor())) {
                ganancias += apuestaColor * 2;
            }
        }

        if (docena == 1) {
            if ("Primera docena".equals(resultado.getDocena())) {
                ganancias += apuestaDocena * 3;
            }
        }
        if (docena == 2) {
            if ("Segunda docena".equals(resultado.getDocena())) {
                ganancias += apuestaDocena * 3;
            }
        }
        if (docena == 3) {
            if ("Tercera docena".equals(resultado.getDocena())) {
                ganancias += apuestaDocena * 3;
            }
        }
        if (ganancias != 0) {
            System.out.println("ENHORABUENA HAS GANADO " + ganancias + " FICHAS!!");
            nuevoSaldo = p.getSaldo() + ganancias;
            p.setSaldo(nuevoSaldo);
        }
        System.out.println("Saldo actual después de jugar: " + p.getSaldo());

        if (nuevoSaldo == 0) {
            System.out.println("¡No tienes suficiente saldo para jugar!");
            return;
        }
    }

    private static void jugarTragaperras(Persona p) {
        // Símbolos de las frutas
        String[] frutas = {";(", ";)", ":0", ":D", ":/"};
        Scanner scanner = new Scanner(System.in);
        System.out.println("Introduce la cantidad de fichas que quiere apostar en la tragaperras (si no quiere apostar a un numero ingrese 0)");
        int apuesta = scanner.nextInt();
        // Actualizar el saldo del jugador después de jugar
        int nuevoSaldo = p.getSaldo() - apuesta;
        // Simular una tirada de tragaperras
        Random random = new Random();
        String resultado = frutas[random.nextInt(frutas.length)] + " | "
                + frutas[random.nextInt(frutas.length)] + " | "
                + frutas[random.nextInt(frutas.length)];
        System.out.println(resultado);
        int ganancias = 0;
        if (resultado.charAt(0) + resultado.charAt(1) == resultado.charAt(3) + resultado.charAt(4) && resultado.charAt(3) + resultado.charAt(4) == resultado.charAt(6) + resultado.charAt(7)) {
            if (";(".equals(resultado.charAt(0))) {
                ganancias += apuesta * 10;
            } else if (";)".equals(resultado.charAt(0))) {
                ganancias += apuesta * 5;
            } else if (":0".equals(resultado.charAt(0))) {
                ganancias += apuesta * 4;
            } else if (":D".equals(resultado.charAt(0))) {
                ganancias += apuesta * 3;
            } else {
                ganancias += apuesta * 2;
            }
        }
        if (ganancias != 0) {
            System.out.println("ENHORABUENA HAS GANADO " + ganancias + " FICHAS!!");
            nuevoSaldo = p.getSaldo() + ganancias;
            p.setSaldo(nuevoSaldo);
        }
        System.out.println("Saldo actual después de jugar: " + p.getSaldo());
        if (nuevoSaldo == 0) {
            System.out.println("¡No tienes suficiente saldo para jugar!");
            return;
        }

        // Supongamos que jugar cuesta 5 unidades
        p.setSaldo(nuevoSaldo);

    }

    private static void cartasTarot(Persona p) {
        String[] frasesTarot = {"... el próximo verano harás un viaje a un lugar muy lejano.", "... tendrás una sorpresa la semana que viene.", "... ganarás mucho dinero mañana."};
        String[] preguntasTarot = {"¿Cual es tu deporte favorito?", "¿Cual es tu ciudad favorita?", "¿Cual es tu profesion?", "¿Cual es tu signo del zodiaco?"};
        Random random = new Random();
        Scanner scanner = new Scanner(System.in);
        System.out.println("El coste de cada tirada de cartas es de 10 euros.");
        if (p.getSaldo() >= 10) {
            System.out.println(preguntasTarot[random.nextInt(preguntasTarot.length)]);
            String signo = scanner.nextLine();
            System.out.println("Siendo " + signo + ", las cartas dicen que" + frasesTarot[random.nextInt(frasesTarot.length)]);

            int nuevoSaldo = p.getSaldo() - 10; // Supongamos que jugar cuesta 10 unidades
            p.setSaldo(nuevoSaldo);
        }else{
            System.out.println("No tienes suficiente saldo para jugar!!");
        }

    }

    private static void consultarSaldo(Persona p) throws IOException {
        System.out.println("Tiene un saldo de " + p.getSaldo() + "$");
    }
}
