package dao;

import model.Venda;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class VendaDAO {

    // Método para registrar uma venda (ajustado para aceitar produtoNome)
    public static boolean registrarVenda(int produtoId, String produtoNome, int quantidade, double valorTotal, LocalDate dataVenda) {
        String sqlVenda = "INSERT INTO vendas (produto_id, produto_nome, quantidade, valor_total, data_venda) VALUES (?, ?, ?, ?, ?)";
        String sqlAtualizarEstoque = "UPDATE produtos SET quantidade = quantidade - ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmtVenda = conn.prepareStatement(sqlVenda);
             PreparedStatement pstmtAtualizarEstoque = conn.prepareStatement(sqlAtualizarEstoque)) {

            // Inserir a venda
            pstmtVenda.setInt(1, produtoId);
            pstmtVenda.setString(2, produtoNome);
            pstmtVenda.setInt(3, quantidade);
            pstmtVenda.setDouble(4, valorTotal);
            pstmtVenda.setString(5, dataVenda.toString());
            pstmtVenda.executeUpdate();

            // Atualizar o estoque
            pstmtAtualizarEstoque.setInt(1, quantidade);
            pstmtAtualizarEstoque.setInt(2, produtoId);
            pstmtAtualizarEstoque.executeUpdate();

            System.out.println("Venda registrada com sucesso.");
            return true;
        } catch (SQLException e) {
            System.out.println("Erro ao registrar venda: " + e.getMessage());
            return false;
        }
    }

    // Método para buscar vendas por período
    public static List<Venda> buscarVendasPorPeriodo(LocalDate dataInicio, LocalDate dataFim) {
        String sql = """
            SELECT vendas.id, vendas.produto_nome, vendas.quantidade, vendas.valor_total, vendas.data_venda
            FROM vendas
            WHERE data_venda BETWEEN ? AND ?
            ORDER BY data_venda;
        """;

        List<Venda> vendas = new ArrayList<>();

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, dataInicio.toString());
            pstmt.setString(2, dataFim.toString());

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Venda venda = new Venda(
                            rs.getInt("id"),
                            rs.getString("produto_nome"),
                            rs.getInt("quantidade"),
                            rs.getDouble("valor_total"),
                            rs.getString("data_venda")
                    );
                    vendas.add(venda);
                }
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar vendas por período: " + e.getMessage());
        }

        return vendas;
    }

    // Método para listar todas as vendas
    public static List<Venda> listarTodasVendas() {
        String sql = """
            SELECT vendas.id, vendas.produto_nome, vendas.quantidade, vendas.valor_total, vendas.data_venda
            FROM vendas
            ORDER BY data_venda;
        """;

        List<Venda> vendas = new ArrayList<>();

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Venda venda = new Venda(
                        rs.getInt("id"),
                        rs.getString("produto_nome"),
                        rs.getInt("quantidade"),
                        rs.getDouble("valor_total"),
                        rs.getString("data_venda")
                );
                vendas.add(venda);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar todas as vendas: " + e.getMessage());
        }

        return vendas;
    }
}