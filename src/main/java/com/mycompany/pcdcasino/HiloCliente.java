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
public class HiloCliente implements Runnable{
    private Socket cliente;

    public HiloCliente(Socket cliente) {
        this.cliente = cliente;
    }
    
    
    @Override
    public void run() {
        try{
            ObjectOutputStream oos = new ObjectOutputStream(cliente.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(cliente.getInputStream());

            Persona p = (Persona) ois.readObject();
            System.out.println("Persona leida." + p.toString());
            
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
                
                Scanner scanner = new Scanner(System.in);
                seleccion = scanner.nextInt();
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
            cliente.close();
            
            
        }catch (IOException | ClassNotFoundException e) {
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
        if (apuestaNumero!=0){
            System.out.println("Introduce el numero al que quiere apostar: ");
            int numeroElegido = scanner.nextInt(); 
        }
           
        System.out.println("Introduce la cantidad de fichas que quiere apostar a ese color (si no quiere apostar a un numero ingrese 0)");
        int apuestaColor = scanner.nextInt();
        if(apuestaColor!=0){
           System.out.println("Introduce el color al que quieres apostar:\n1.Rojo\n2.Negro ");
        int color = scanner.nextInt(); 
        }
        
        System.out.println("Introduce la cantidad de fichas que quiere apostar a esa docena (si no quiere apostar a un numero ingrese 0)");
        int apuestaDocena = scanner.nextInt();
        if(apuestaDocena!=0){
            System.out.println("Introduce la docena a la que quieres apostar:\n1.Primera docena\n2.Segunda docena\n3.Tercera docena ");
        int docena = scanner.nextInt();
        }
        
        
        int nuevoSaldo = p.getSaldo() - (apuestaNumero + apuestaColor + apuestaDocena); // Supongamos que jugar cuesta 10 unidades
        
        
        //Obtener resultado (numero, color y docena)
        int[] rojos = {1, 3, 5, 7, 9, 12, 14, 16, 18, 19, 21, 23, 25, 27, 30, 32, 34, 36};
        int[] negros = {2, 4, 6, 8, 10, 11, 13, 15, 17, 20, 22, 24, 26, 28, 29 , 31, 33, 35};
        
        
        Random random = new Random();
        int resultado = random.nextInt(37); // Números del 0 al 36
        
        String colorResultado = null;
        String docenaResultado = null;
        
        for (int i = 0; i < rojos.length; i++) {
            if (rojos[i] == resultado) {
                colorResultado = "Rojo";
                break; 
            }
            if (negros[i] == resultado) {
                colorResultado = "Negro";
                break; 
            }
            else{
                colorResultado = "Verde";
            }
            if(0<resultado && resultado<13){
                docenaResultado = "Primera docena";
            }
            if(12<resultado && resultado<25){
                docenaResultado = "Segunda docena";
            }
            if(24<resultado && resultado<37){
                docenaResultado = "Tercera docena";
            }
        }
        
        System.out.println("La ruleta ha caído en el número: " + resultado + ", " + colorResultado + ", " + docenaResultado);
        int numeroElegido = 0;

        //Calcular el saldo tras la tirada
        
        
        
        
        p.setSaldo(nuevoSaldo);
        System.out.println("Saldo actual después de jugar: " + p.getSaldo());
        
        if (nuevoSaldo == 0) {
            System.out.println("¡No tienes suficiente saldo para jugar!");
            return;
        }
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
    

    private static void consultarSaldo(Persona p, ObjectOutputStream oos) throws IOException {
        oos.writeObject("CONSULTAR_SALDO");
        oos.writeObject(p);
    }
}
