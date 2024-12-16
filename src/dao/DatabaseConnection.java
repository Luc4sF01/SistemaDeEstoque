package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {
    // Caminho absoluto para o banco de dados
    private static final String URL = "jdbc:sqlite:D:/ControleDeEstoque/estoque.db";

    // Método para estabelecer conexão com o banco
    public static Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(URL);
            System.out.println("Conexão estabelecida com o banco: " + URL); // Exibe o caminho do banco
        } catch (SQLException e) {
            System.out.println("Erro ao conectar com o banco de dados: " + e.getMessage());
        }
        return conn;
    }

    // Método para criar as tabelas necessárias
    public static void createTable() {
        String sqlProdutos = """
                    CREATE TABLE IF NOT EXISTS produtos (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        nome TEXT NOT NULL,
                        preco REAL NOT NULL,
                        quantidade INTEGER NOT NULL
                    );
                """;

        String sqlVendas = """
                    CREATE TABLE IF NOT EXISTS vendas (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        produto_id INTEGER NOT NULL,
                        quantidade INTEGER NOT NULL,
                        valor_total REAL NOT NULL,
                        data_venda TEXT NOT NULL,
                        FOREIGN KEY (produto_id) REFERENCES produtos(id)
                    );
                """;

        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sqlProdutos);
            System.out.println("Tabela Produtos criada com sucesso!");

            stmt.execute(sqlVendas);
            System.out.println("Tabela Vendas criada com sucesso!");

            // Adiciona a coluna produto_nome, caso ela ainda não exista
            adicionarColunaProdutoNome();
        } catch (SQLException e) {
            System.out.println("Erro ao criar tabelas: " + e.getMessage());
        }
    }

    // Método para adicionar a coluna 'produto_nome' na tabela vendas
    public static void adicionarColunaProdutoNome() {
        String sql = "ALTER TABLE vendas ADD COLUMN produto_nome TEXT";

        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
            System.out.println("Coluna 'produto_nome' adicionada com sucesso!");
        } catch (SQLException e) {
            // Verifica se a coluna já existe
            if (e.getMessage().contains("duplicate column name") || e.getMessage().contains("already exists")) {
                System.out.println("A coluna 'produto_nome' já existe na tabela vendas.");
            } else {
                System.out.println("Erro ao adicionar a coluna 'produto_nome': " + e.getMessage());
            }
        }
    }
}
