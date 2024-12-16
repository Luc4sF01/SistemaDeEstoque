package controller;

import dao.DatabaseConnection;
import dao.ProdutoDAO;
import model.Produto;
import view.MainFrame;

import javax.swing.SwingUtilities;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("===== Iniciando o Sistema de Estoque =====");

        // Configurar o Look and Feel (FlatLaf)
        try {
            com.formdev.flatlaf.FlatDarculaLaf.setup();
            System.out.println("Tema Dark FlatLaf aplicado com sucesso!");
        } catch (Exception e) {
            System.err.println("Erro ao aplicar o Look and Feel: " + e.getMessage());
        }

        // Conectar ao banco de dados e criar tabelas
        System.out.println("\n>>> Conectando ao banco de dados e criando tabelas...");
        try {
            DatabaseConnection.createTable();
            System.out.println("Banco de dados configurado com sucesso!");
        } catch (Exception e) {
            System.err.println("Erro ao configurar o banco de dados: " + e.getMessage());
            e.printStackTrace();
            return; // Finaliza o programa em caso de falha crítica
        }

        // Testar cadastro de produto e listar produtos
        System.out.println("\n>>> Cadastrando produto de teste...");
        Produto produto = new Produto("Shampoo", 19.99, 50);
        ProdutoDAO.cadastrarProduto(produto);

        System.out.println("\n>>> Listando produtos cadastrados...");
        List<Produto> produtos = ProdutoDAO.listarProdutos();
        if (produtos.isEmpty()) {
            System.out.println("Nenhum produto cadastrado.");
        } else {
            for (Produto p : produtos) {
                System.out.println("ID: " + p.getId() + ", Nome: " + p.getNome() + ", Preço: " + p.getPreco() + ", Quantidade: " + p.getQuantidade());
            }
        }

        // Inicializar a interface gráfica
        System.out.println("\n>>> Inicializando a interface gráfica...");
        SwingUtilities.invokeLater(() -> {
            try {
                new MainFrame(); // Abre a GUI
                System.out.println("Interface gráfica inicializada com sucesso!");
            } catch (Exception e) {
                System.err.println("Erro ao inicializar a interface gráfica: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }
}