package dao;

import model.Venda;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VendaDAO {

    // Método para registrar uma venda
    public static boolean registrarVenda(int produtoId, String produtoNome, int quantidade, double valorTotal, String dataVenda) {
        String sqlVenda = "INSERT INTO vendas (produto_id, produto_nome, quantidade, valor_total, data_venda) VALUES (?, ?, ?, ?, ?)";
        String sqlAtualizarEstoque = "UPDATE produtos SET quantidade = quantidade - ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmtVenda = conn.prepareStatement(sqlVenda);
             PreparedStatement pstmtAtualizarEstoque = conn.prepareStatement(sqlAtualizarEstoque)) {

            // Verifica se o produto tem estoque suficiente antes da venda
            if (!temEstoqueSuficiente(produtoId, quantidade)) {
                System.out.println("Estoque insuficiente para o produto: " + produtoNome);
                return false;
            }

            // Inserir a venda
            pstmtVenda.setInt(1, produtoId);
            pstmtVenda.setString(2, produtoNome);
            pstmtVenda.setInt(3, quantidade);
            pstmtVenda.setDouble(4, valorTotal);
            pstmtVenda.setString(5, dataVenda);
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

    // Método para verificar se o produto tem estoque suficiente
    private static boolean temEstoqueSuficiente(int produtoId, int quantidade) {
        String sql = "SELECT quantidade FROM produtos WHERE id = ?";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, produtoId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                int estoqueAtual = rs.getInt("quantidade");
                return estoqueAtual >= quantidade;
            }
        } catch (SQLException e) {
            System.out.println("Erro ao verificar estoque: " + e.getMessage());
        }
        return false;
    }

    // Método para listar todas as vendas
    public static List<Venda> listarVendas() {
        String sql = "SELECT id, produto_nome, quantidade, valor_total, data_venda FROM vendas ORDER BY data_venda DESC";
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
            System.out.println("Erro ao listar vendas: " + e.getMessage());
        }
        return vendas;
    }

    // Método para listar vendas por período (Corrigido para receber Strings)
    public static List<Venda> listarVendasPorData(String dataInicio, String dataFim) {
        String sql = """
            SELECT id, produto_nome, quantidade, valor_total, data_venda
            FROM vendas
            WHERE data_venda BETWEEN ? AND ?
            ORDER BY data_venda DESC;
        """;

        List<Venda> vendas = new ArrayList<>();

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, dataInicio);
            pstmt.setString(2, dataFim);

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

    // Método para remover uma venda por ID
    public static boolean removerVenda(int vendaId) {
        String sql = "DELETE FROM vendas WHERE id = ?";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, vendaId);
            int rowsAffected = pstmt.executeUpdate();

            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println("Erro ao remover venda: " + e.getMessage());
            return false;
        }
    }
}
