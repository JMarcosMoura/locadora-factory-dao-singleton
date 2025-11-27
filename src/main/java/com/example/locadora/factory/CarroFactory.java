package com.example.locadora.factory;

import com.example.locadora.model.Carro;
import com.example.locadora.model.Veiculo;

public class CarroFactory extends VeiculoFactory {
    @Override
    public Veiculo criar(String placa, String marca, String modelo, int ano, double diaria, int extra, String extraText) {
        int portas = extra;
        String combustivel = extraText == null ? "" : extraText;
        return new Carro(placa, marca, modelo, ano, diaria, portas, combustivel);
    }
}
