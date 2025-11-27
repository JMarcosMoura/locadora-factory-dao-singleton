package com.example.locadora.factory;

import com.example.locadora.dao.*;

public class DAOFactory {

    private static ClienteDAO clienteDAO;
    private static VeiculoDAO veiculoDAO;
    private static AluguelDAO aluguelDAO;
    private static FuncionarioDAO funcionarioDAO;

    public static ClienteDAO clienteDAO() {
        if (clienteDAO == null) clienteDAO = new ClienteDAO();
        return clienteDAO;
    }

    public static VeiculoDAO veiculoDAO() {
        if (veiculoDAO == null) veiculoDAO = new VeiculoDAO();
        return veiculoDAO;
    }

    public static AluguelDAO aluguelDAO() {
        if (aluguelDAO == null) aluguelDAO = new AluguelDAO();
        return aluguelDAO;
    }

    public static FuncionarioDAO funcionarioDAO() {
        if (funcionarioDAO == null) funcionarioDAO = new FuncionarioDAO();
        return funcionarioDAO;
    }
}
