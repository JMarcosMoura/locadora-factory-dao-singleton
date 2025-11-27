package com.example.locadora.factory;

import com.example.locadora.model.Moto;
import com.example.locadora.model.Veiculo;

public class MotoFactory extends VeiculoFactory {
    @Override
    public Veiculo criar(String placa, String marca, String modelo, int ano, double diaria, int extra, String extraText) {
        int cilindradas = extra;
        return new Moto(placa, marca, modelo, ano, diaria, cilindradas);
    }
}
