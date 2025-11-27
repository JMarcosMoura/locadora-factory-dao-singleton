package com.example.locadora.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "moto")
public class Moto extends Veiculo {
    private int cilindradas;

    public Moto() {}
    public Moto(String placa, String marca, String modelo, int ano, double valorDiaria, int cilindradas) {
        super(placa, marca, modelo, ano, valorDiaria);
        this.cilindradas = cilindradas;
    }
    public int getCilindradas() { return cilindradas; }
    public void setCilindradas(int cilindradas) { this.cilindradas = cilindradas; }
}
