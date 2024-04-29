package com.mycompany.pcdcasino;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class PCDCasino {

    public static void main(String[] args) {
        try {
            
            ServerSocket serverSocket = new ServerSocket(44444);
            System.out.println("Esperando a un cliente");

            while (true) {
                Socket cliente = serverSocket.accept();
                Thread hiloCliente = new Thread(new HiloCliente(cliente));
                hiloCliente.start();
                System.out.println("Cliente conectado");
            }
        } catch (IOException e) {
            System.err.println("IOException: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}
