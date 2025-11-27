package com.example.locadora.model;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "funcionario")
public class Funcionario extends Pessoa {
    private String cargo;
    private double salario;

    @ManyToMany
    @JoinTable(name = "funcionario_aluguel",
        joinColumns = @JoinColumn(name = "funcionario_id"),
        inverseJoinColumns = @JoinColumn(name = "aluguel_id"))
    private Set<Aluguel> alugueis = new HashSet<>();

    public Funcionario() {}
    public Funcionario(String nome, String cpf, String telefone, String cargo, double salario) {
        super(nome, cpf, telefone);
        this.cargo = cargo;
        this.salario = salario;
    }
    public String getCargo() { return cargo; }
    public void setCargo(String cargo) { this.cargo = cargo; }
    public double getSalario() { return salario; }
    public void setSalario(double salario) { this.salario = salario; }
    public Set<Aluguel> getAlugueis() { return alugueis; }
    public void addAluguel(Aluguel a) { alugueis.add(a); a.getFuncionarios().add(this); }
}
