package com.mycompany.pcdcasino;

import java.io.Serializable;

public class Persona implements Serializable {
    private int edad;
    private String nombre;
    private int saldo;

    public Persona(int edad, String nombre, int saldo) throws EdadException {
        if (edad < 18) {
            throw new EdadException("La edad no puede ser menor que 18");
        }
        this.edad = edad;
        this.nombre = nombre;
        this.saldo = saldo;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getSaldo() {
        return saldo;
    }

    public void setSaldo(int saldo) {
        this.saldo = saldo;
    }

    @Override
    public String toString() {
        return "El cliente " + nombre + " tiene " + edad + " años y un saldo de " + saldo + " unidades.";
    }
}
