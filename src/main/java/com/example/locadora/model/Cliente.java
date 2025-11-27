package com.example.locadora.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cliente")
public class Cliente extends Pessoa {
    private String cnh;

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Aluguel> alugueis = new ArrayList<>();

    public Cliente() {}
    public Cliente(String nome, String cpf, String telefone, String cnh) {
        super(nome, cpf, telefone);
        this.cnh = cnh;
    }

    public String getCnh() { return cnh; }
    public void setCnh(String cnh) { this.cnh = cnh; }
    public List<Aluguel> getAlugueis() { return alugueis; }
    public void addAluguel(Aluguel a) { alugueis.add(a); a.setCliente(this); }
}
