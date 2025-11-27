package com.example.locadora.factory;

import com.example.locadora.model.Veiculo;

public abstract class VeiculoFactory {
    public abstract Veiculo criar(String placa, String marca, String modelo, int ano, double diaria, int extra, String extraText);
}
