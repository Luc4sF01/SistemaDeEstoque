package dao;

import model.Produto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProdutoDAO {

    // Método para verificar se o produto já existe
    public static boolean produtoExiste(String nome) {
        String sql = "SELECT COUNT(*) AS total FROM produtos WHERE nome = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, nome);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next() && rs.getInt("total") > 0) {
                return true; // Produto já existe
            }
        } catch (SQLException e) {
            System.err.println("Erro ao verificar existência do produto: " + e.getMessage());
        }
        return false; // Produto não existe
    }

    // Método para cadastrar um produto
    public static void cadastrarProduto(Produto produto) {
        if (produtoExiste(produto.getNome())) {
            System.out.println("Erro: Produto com o nome '" + produto.getNome() + "' já está cadastrado.");
            return;
        }

        String sql = "INSERT INTO produtos(nome, preco, quantidade) VALUES(?, ?, ?)";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, produto.getNome());
            pstmt.setDouble(2, produto.getPreco());
            pstmt.setInt(3, produto.getQuantidade());

            pstmt.executeUpdate();
            System.out.println("Produto cadastrado com sucesso!");
        } catch (SQLException e) {
            System.err.println("Erro ao cadastrar produto: " + e.getMessage());
        }
    }

    // Método para listar todos os produtos
    public static List<Produto> listarProdutos() {
        String sql = "SELECT * FROM produtos";
        List<Produto> produtos = new ArrayList<>();

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Produto produto = new Produto(
                        rs.getString("nome"),
                        rs.getDouble("preco"),
                        rs.getInt("quantidade")
                );
                produto.setId(rs.getInt("id")); // Certifique-se de capturar o ID do produto
                produtos.add(produto);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar produtos: " + e.getMessage());
        }

        return produtos;
    }

    // Método para buscar um produto pelo nome
    public static Produto buscarPorNome(String nome) {
        String sql = "SELECT * FROM produtos WHERE nome = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, nome);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                Produto produto = new Produto(
                        rs.getString("nome"),
                        rs.getDouble("preco"),
                        rs.getInt("quantidade")
                );
                produto.setId(rs.getInt("id"));
                return produto;
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar produto por nome: " + e.getMessage());
        }
        return null;
    }

    // Método para atualizar um produto
    public static void atualizarProduto(Produto produto) {
        String sql = "UPDATE produtos SET nome = ?, preco = ?, quantidade = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, produto.getNome());
            pstmt.setDouble(2, produto.getPreco());
            pstmt.setInt(3, produto.getQuantidade());
            pstmt.setInt(4, produto.getId()); // Certifique-se de passar o ID corretamente

            pstmt.executeUpdate();
            System.out.println("Produto atualizado com sucesso!");
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar produto: " + e.getMessage());
        }
    }

    // Método para excluir um produto (agora estático)
    public static void excluirProduto(int id) {
        String sql = "DELETE FROM produtos WHERE id = ?";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            System.out.println("Produto excluído com sucesso!");
        } catch (SQLException e) {
            System.err.println("Erro ao excluir produto: " + e.getMessage());
        }
    }

    // Método para atualizar a quantidade do produto
    public static boolean atualizarQuantidade(int id, int novaQuantidade) {
        String sql = "UPDATE produtos SET quantidade = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, novaQuantidade);
            pstmt.setInt(2, id);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar quantidade: " + e.getMessage());
            return false;
        }
    }
}
