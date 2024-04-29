/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.pcdcasino;

/**
 *
 * @author javis
 */
public class NumeroRule {
    private int numero;
    private String color;
    private String docena;

    public NumeroRule(int numero, String color, String docena) {
        this.numero = numero;
        this.color = color;
        this.docena = docena;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getDocena() {
        return docena;
    }

    public void setDocena(String docena) {
        this.docena = docena;
    }

    @Override
    public String toString() {
        return "NumeroRule{" + "numero=" + numero + ", color=" + color + ", docena=" + docena + '}';
    }
    
    
}
