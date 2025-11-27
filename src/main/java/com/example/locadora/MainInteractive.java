package com.example.locadora;

import com.example.locadora.dao.*;
import com.example.locadora.factory.*;
import com.example.locadora.model.*;
import com.example.locadora.util.JPAUtil;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
//import jakarta.persistence.EntityManager;
//import jakarta.persistence.TypedQuery;

public class MainInteractive {
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        // ensure sample data inserted using DAOs
        ClienteDAO clienteDAO = new ClienteDAO();
        VeiculoDAO veiculoDAO = new VeiculoDAO();
        FuncionarioDAO funcionarioDAO = new FuncionarioDAO();
        AluguelDAO aluguelDAO = new AluguelDAO();

        if (clienteDAO.findAll().isEmpty()) {
            Cliente c = new Cliente("ClienteExemplo", "00011122233", "99999-0000", "CNH0000");
            clienteDAO.save(c);
            CarroFactory cf = new CarroFactory();
            Veiculo v = cf.criar("EXM-0001", "Toyota", "Corolla", 2021, 150.0, 4, "Gasolina");
            veiculoDAO.save(v);
            Funcionario f = new Funcionario("FuncionarioEx", "11122233344", "98888-1111", "Atendente", 2000.0);
            funcionarioDAO.save(f);
            System.out.println("Dados de exemplo inseridos: 1 cliente, 1 veiculo, 1 funcionario");
        }

        boolean running = true;
        while (running) {
            System.out.println();
            System.out.println("========================================");
            System.out.println("       LOCADORA DE VEICULOS - MENU      ");
            System.out.println("========================================");
            System.out.println("1 - CADASTRAR CLIENTE");
            System.out.println("2 - CADASTRAR VEICULO");
            System.out.println("3 - LISTAR VEICULOS");
            System.out.println("4 - REALIZAR ALUGUEL");
            System.out.println("5 - DEVOLVER VEICULO");
            System.out.println("6 - LISTAR ALUGUEIS");
            System.out.println("0 - SAIR");
            System.out.print("ESCOLHA: ");
            String line = scanner.nextLine();
            int opcao = -1;
            try { opcao = Integer.parseInt(line); } catch (Exception ex) { opcao = -1; }

            switch (opcao) {
                case 1: cadastrarCliente(clienteDAO); break;
                case 2: cadastrarVeiculo(veiculoDAO); break;
                case 3: listarVeiculos(veiculoDAO); break;
                case 4: realizarAluguel(clienteDAO, veiculoDAO, aluguelDAO); break;
                case 5: devolverVeiculo(aluguelDAO); break;
                case 6: listarAlugueis(aluguelDAO); break;
                case 0: running = false; break;
                default: System.out.println("Opcao invalida");
            }
        }

        JPAUtil.closeFactory();
        System.out.println("Programa finalizado");
    }

    private static void cadastrarCliente(ClienteDAO clienteDAO) {
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        System.out.print("CPF: ");
        String cpf = scanner.nextLine();
        System.out.print("Telefone: ");
        String tel = scanner.nextLine();
        System.out.print("CNH: ");
        String cnh = scanner.nextLine();
        Cliente c = new Cliente(nome, cpf, tel, cnh);
        clienteDAO.save(c);
        System.out.println("Cliente cadastrado: " + c.getNome());
    }

    private static void cadastrarVeiculo(VeiculoDAO veiculoDAO) {
        System.out.print("Tipo (1=Carro, 2=Moto): ");
        String t = scanner.nextLine();
        if (!t.equals("1") && !t.equals("2")) { System.out.println("Tipo invalido"); return; }
        System.out.print("Placa: ");
        String placa = scanner.nextLine();
        System.out.print("Marca: ");
        String marca = scanner.nextLine();
        System.out.print("Modelo: ");
        String modelo = scanner.nextLine();
        System.out.print("Ano: ");
        int ano = Integer.parseInt(scanner.nextLine());
        System.out.print("Valor diaria: ");
        double diaria = Double.parseDouble(scanner.nextLine());

        if (t.equals("1")) {
            System.out.print("Numero portas: ");
            int portas = Integer.parseInt(scanner.nextLine());
            System.out.print("Tipo combustivel: ");
            String comb = scanner.nextLine();
            CarroFactory cf = new CarroFactory();
            Veiculo v = cf.criar(placa, marca, modelo, ano, diaria, portas, comb);
            veiculoDAO.save(v);
            System.out.println("Carro cadastrado: " + modelo + " (" + placa + ")");
        } else {
            System.out.print("Cilindradas: ");
            int cil = Integer.parseInt(scanner.nextLine());
            MotoFactory mf = new MotoFactory();
            Veiculo v = mf.criar(placa, marca, modelo, ano, diaria, cil, null);
            veiculoDAO.save(v);
            System.out.println("Moto cadastrada: " + modelo + " (" + placa + ")");
        }
    }

    private static void listarVeiculos(VeiculoDAO veiculoDAO) {
        List<Veiculo> list = veiculoDAO.findAll();
        System.out.println();
        System.out.println("=== LISTA DE VEICULOS ===");
        System.out.println(String.format("%-4s %-15s %-18s %-12s %-10s %s", "ID", "MARCA", "MODELO", "PLACA", "DIARIA", "STATUS"));
        System.out.println("------------------------------------------------------------------");
        int idx = 0;
        for (Veiculo v : list) {
            String status = v.isDisponivel() ? "DISPONIVEL" : "ALUGADO";
            String preco = String.format(Locale.US, "%.2f", v.getValorDiaria());
            System.out.println(String.format("%-4d %-15s %-18s %-12s R$%8s %s", idx, v.getMarca(), v.getModelo(), v.getPlaca(), preco, status));
            idx++;
        }
        System.out.println("------------------------------------------------------------------");
    }

    private static void realizarAluguel(ClienteDAO clienteDAO, VeiculoDAO veiculoDAO, AluguelDAO aluguelDAO) {
        List<Cliente> clientes = clienteDAO.findAll();
        if (clientes.isEmpty()) { System.out.println("Nenhum cliente cadastrado"); return; }
        System.out.println();
        System.out.println("=== CLIENTES ===");
        for (int i = 0; i < clientes.size(); i++) System.out.println(String.format("%d - %s", i, clientes.get(i).getNome()));
        int ci;
        try {
            System.out.print("Escolha cliente (indice): ");
            ci = Integer.parseInt(scanner.nextLine());
            if (ci < 0 || ci >= clientes.size()) { System.out.println("Indice de cliente invalido"); return; }
        } catch (NumberFormatException e) { System.out.println("Entrada invalida"); return; }
        Cliente cliente = clientes.get(ci);

        List<Veiculo> veiculos = veiculoDAO.findDisponiveis();
        if (veiculos.isEmpty()) { System.out.println("Nenhum veiculo disponivel"); return; }

        System.out.println();
        System.out.println("=== VEICULOS DISPONIVEIS ===");
        System.out.println(String.format("%-4s %-15s %-18s %-12s %-10s", "ID", "MARCA", "MODELO", "PLACA", "DIARIA"));
        System.out.println("-------------------------------------------------------------");
        for (int i = 0; i < veiculos.size(); i++) {
            Veiculo v = veiculos.get(i);
            String preco = String.format(Locale.US, "%.2f", v.getValorDiaria());
            System.out.println(String.format("%-4d %-15s %-18s %-12s R$%8s", i, v.getMarca(), v.getModelo(), v.getPlaca(), preco));
        }
        System.out.println("-------------------------------------------------------------");

        int vi;
        try {
            System.out.print("Escolha veiculo (indice): ");
            vi = Integer.parseInt(scanner.nextLine());
            if (vi < 0 || vi >= veiculos.size()) { System.out.println("Indice de veiculo invalido"); return; }
        } catch (NumberFormatException e) { System.out.println("Entrada invalida"); return; }
        Veiculo veiculo = veiculos.get(vi);

        int dias;
        try {
            System.out.print("Quantidade dias: ");
            dias = Integer.parseInt(scanner.nextLine());
            if (dias <= 0) { System.out.println("Quantidade de dias invalida"); return; }
        } catch (NumberFormatException e) { System.out.println("Entrada invalida"); return; }

        double valor = veiculo.getValorDiaria() * dias;
        String precoTotal = String.format(Locale.US, "%.2f", valor);

        // persistir aluguel e atualizar veiculo via DAO
        Aluguel a = new Aluguel();
        a.setCliente(cliente);
        a.setVeiculo(veiculo);
        a.setDataInicio(LocalDate.now());
        a.setDataFim(LocalDate.now().plusDays(dias));
        a.setValorTotal(valor);
        aluguelDAO.save(a);

        veiculo.setDisponivel(false);
        veiculoDAO.update(veiculo);

        System.out.println();
        System.out.println("=== RESUMO DO ALUGUEL ===");
        System.out.println("Cliente: " + cliente.getNome());
        System.out.println("Veiculo: " + veiculo.getMarca() + " " + veiculo.getModelo() + " (" + veiculo.getPlaca() + ")");
        System.out.println("Periodo: " + a.getDataInicio() + " a " + a.getDataFim());
        System.out.println("Valor total: R$" + precoTotal);
        System.out.println("=========================");
    }

    private static void devolverVeiculo(AluguelDAO aluguelDAO) {
        List<Aluguel> list = aluguelDAO.findAll();
        if (list.isEmpty()) { System.out.println("Nenhum aluguel encontrado"); return; }
        System.out.println("Alugueis:");
        for (int i=0;i<list.size();i++) {
            Aluguel a = list.get(i);
            System.out.println(i + " - " + a.getCliente().getNome() + " | " + a.getVeiculo().getModelo() + " (" + a.getVeiculo().getPlaca() + ")");
        }
        System.out.print("Escolha aluguel para devolver (indice): ");
        int ai = Integer.parseInt(scanner.nextLine());
        Aluguel a = list.get(ai);

        // reativa veiculo e remove aluguel
        Veiculo v = a.getVeiculo();
        v.setDisponivel(true);
        // update via DAO: need VeiculoDAO instance
        VeiculoDAO veiculoDAO = new VeiculoDAO();
        veiculoDAO.update(v);
        aluguelDAO.delete(a.getId());
        System.out.println("Veiculo devolvido e aluguel removido do sistema");
    }

    private static void listarAlugueis(AluguelDAO aluguelDAO) {
        List<Aluguel> list = aluguelDAO.findAll();
        System.out.println();
        System.out.println("=== ALUGUEIS ===");
        for (Aluguel a : list) {
            System.out.println(a.getId() + " - Cliente: " + a.getCliente().getNome() +
                " | Veiculo: " + a.getVeiculo().getMarca() + " " + a.getVeiculo().getModelo() +
                " | Inicio: " + a.getDataInicio() + " | Fim: " + a.getDataFim() +
                " | Valor: R$" + String.format(Locale.US, "%.2f", a.getValorTotal()));
        }
    }
}
