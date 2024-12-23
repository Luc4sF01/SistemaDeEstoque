package dao;

import model.Produto;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.*;
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

    // Método para buscar um produto pelo ID
    public static Produto buscarPorId(int id) {
        String sql = "SELECT * FROM produtos WHERE id = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                Produto produto = new Produto(
                        rs.getString("nome"),
                        rs.getDouble("preco"),
                        rs.getInt("quantidade")
                );
                produto.setId(rs.getInt("id"));
                return produto; // Retorna o produto encontrado
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar produto por ID: " + e.getMessage());
        }
        return null; // Retorna null se não encontrar
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

    // Método para listar todos os produtos
    public static List<Produto> listarProdutos() {
        String sql = "SELECT * FROM produtos ORDER BY nome"; // Adiciona ordenação alfabética
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
                produto.setId(rs.getInt("id")); // Captura o ID do produto
                produtos.add(produto);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar produtos: " + e.getMessage());
        }

        return produtos;
    }


    // Método para atualizar um produto
    public static void atualizarProduto(Produto produto) {
        String sql = "UPDATE produtos SET nome = ?, preco = ?, quantidade = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, produto.getNome());
            pstmt.setDouble(2, produto.getPreco());
            pstmt.setInt(3, produto.getQuantidade());
            pstmt.setInt(4, produto.getId());

            pstmt.executeUpdate();
            System.out.println("Produto atualizado com sucesso!");
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar produto: " + e.getMessage());
        }
    }

    // Método para excluir um produto
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

    // Método para importar dados do CSV
    public static void importarCSV(String caminhoArquivo) {
        String sqlInsert = "INSERT INTO produtos (nome, preco, quantidade) VALUES (?, ?, ?)";
        String sqlCheck = "SELECT COUNT(*) AS total FROM produtos WHERE nome = ?";

        try (BufferedReader br = new BufferedReader(new FileReader(caminhoArquivo));
             Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmtInsert = conn.prepareStatement(sqlInsert);
             PreparedStatement pstmtCheck = conn.prepareStatement(sqlCheck)) {

            String linha;
            boolean primeiraLinha = true;

            while ((linha = br.readLine()) != null) {
                if (primeiraLinha) {
                    primeiraLinha = false; // Ignorar cabeçalho
                    continue;
                }

                if (linha.contains("TOTAL:") || linha.trim().isEmpty()) {
                    continue;
                }

                String[] valores = linha.split(",");

                if (valores.length >= 6) {
                    String nome = valores[1].trim();
                    String valorVendaStr = valores[5].trim().replace("R$", "").replace(",", ".");
                    double preco;
                    int quantidade;

                    try {
                        preco = Double.parseDouble(valorVendaStr);
                        quantidade = Integer.parseInt(valores[0].trim());
                    } catch (NumberFormatException e) {
                        System.err.println("Linha ignorada (formato inválido): " + linha);
                        continue;
                    }

                    pstmtCheck.setString(1, nome);
                    ResultSet rs = pstmtCheck.executeQuery();

                    if (rs.next() && rs.getInt("total") > 0) {
                        System.out.println("Produto duplicado encontrado e ignorado: " + nome);
                        continue;
                    }

                    pstmtInsert.setString(1, nome);
                    pstmtInsert.setDouble(2, preco);
                    pstmtInsert.setInt(3, quantidade);
                    pstmtInsert.executeUpdate();
                }
            }
            System.out.println("Importação concluída!");
        } catch (Exception e) {
            System.err.println("Erro ao importar CSV: " + e.getMessage());
        }
    }
}
