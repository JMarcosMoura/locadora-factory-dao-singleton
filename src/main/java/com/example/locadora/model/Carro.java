package com.example.locadora.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "carro")
public class Carro extends Veiculo {
    private int numeroPortas;
    private String tipoCombustivel;

    public Carro() {}
    public Carro(String placa, String marca, String modelo, int ano, double valorDiaria, int numeroPortas, String tipoCombustivel) {
        super(placa, marca, modelo, ano, valorDiaria);
        this.numeroPortas = numeroPortas; this.tipoCombustivel = tipoCombustivel;
    }
    public int getNumeroPortas() { return numeroPortas; }
    public void setNumeroPortas(int numeroPortas) { this.numeroPortas = numeroPortas; }
    public String getTipoCombustivel() { return tipoCombustivel; }
    public void setTipoCombustivel(String tipoCombustivel) { this.tipoCombustivel = tipoCombustivel; }
}
