package com.example.locadora.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "veiculo")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Veiculo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String placa;
    private String marca;
    private String modelo;
    private int ano;
    private double valorDiaria;
    private boolean disponivel = true;

    @OneToMany(mappedBy = "veiculo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Aluguel> alugueis = new ArrayList<>();

    public Veiculo() {}
    public Veiculo(String placa, String marca, String modelo, int ano, double valorDiaria) {
        this.placa = placa; this.marca = marca; this.modelo = modelo; this.ano = ano; this.valorDiaria = valorDiaria;
    }
    public Long getId() { return id; }
    public String getPlaca() { return placa; }
    public void setPlaca(String placa) { this.placa = placa; }
    public String getMarca() { return marca; }
    public String getModelo() { return modelo; }
    public int getAno() { return ano; }
    public double getValorDiaria() { return valorDiaria; }
    public boolean isDisponivel() { return disponivel; }
    public void setDisponivel(boolean disponivel) { this.disponivel = disponivel; }
    public List<Aluguel> getAlugueis() { return alugueis; }
    public void addAluguel(Aluguel a) { alugueis.add(a); a.setVeiculo(this); }
}
