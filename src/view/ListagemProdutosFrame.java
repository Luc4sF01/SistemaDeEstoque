package view;

import dao.ProdutoDAO;
import model.Produto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ListagemProdutosFrame extends JFrame {

    public ListagemProdutosFrame() {
        // Configurações da janela
        setTitle("Listagem de Produtos");
        setSize(800, 600); // Ajuste para tamanho maior
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null); // Centraliza a janela

        // Título da tabela
        JLabel titleLabel = new JLabel("Produtos Cadastrados", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(titleLabel, BorderLayout.NORTH);

        // Tabela de produtos
        String[] colunas = {"ID", "Nome", "Preço (R$)", "Quantidade"};
        DefaultTableModel tableModel = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Impede a edição direta das células
            }
        };
        JTable tabela = new JTable(tableModel);
        tabela.setFont(new Font("Arial", Font.PLAIN, 14));
        tabela.setRowHeight(25);
        tabela.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // Seleção de apenas uma linha

        // Preencher a tabela com os dados do banco
        atualizarTabela(tableModel);

        // Adicionar a tabela a um painel com barra de rolagem
        JScrollPane scrollPane = new JScrollPane(tabela);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(scrollPane, BorderLayout.CENTER);

        // Botões de ação
        JButton btnAtualizar = new JButton("Atualizar Produto");
        JButton btnExcluir = new JButton("Excluir Produto");
        JButton btnFechar = new JButton("Fechar");

        // Estilo dos botões
        btnAtualizar.setFont(new Font("Arial", Font.PLAIN, 14));
        btnExcluir.setFont(new Font("Arial", Font.PLAIN, 14));
        btnFechar.setFont(new Font("Arial", Font.PLAIN, 14));

        // Ação do botão "Atualizar Produto"
        btnAtualizar.addActionListener(e -> {
            int selectedRow = tabela.getSelectedRow();
            if (selectedRow >= 0) {
                try {
                    int id = (int) tabela.getValueAt(selectedRow, 0);
                    String nome = (String) tabela.getValueAt(selectedRow, 1);
                    double preco = (double) tabela.getValueAt(selectedRow, 2);
                    int quantidade = (int) tabela.getValueAt(selectedRow, 3);

                    Produto produtoSelecionado = new Produto(nome, preco, quantidade);
                    produtoSelecionado.setId(id);

                    // Abre a janela de atualização
                    AtualizarProdutoFrame atualizarFrame = new AtualizarProdutoFrame(produtoSelecionado);

                    // Atualiza a tabela após fechar a janela de atualização
                    atualizarFrame.addWindowListener(new java.awt.event.WindowAdapter() {
                        @Override
                        public void windowClosed(java.awt.event.WindowEvent e) {
                            atualizarTabela(tableModel); // Recarrega os dados na tabela
                        }
                    });
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(ListagemProdutosFrame.this,
                            "Erro ao abrir a janela de atualização: " + ex.getMessage(),
                            "Erro", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(ListagemProdutosFrame.this,
                        "Por favor, selecione um produto para atualizar.", "Erro", JOptionPane.WARNING_MESSAGE);
            }
        });

        // Ação do botão "Excluir Produto"
        btnExcluir.addActionListener(e -> {
            int selectedRow = tabela.getSelectedRow();
            if (selectedRow >= 0) {
                try {
                    int id = (int) tabela.getValueAt(selectedRow, 0);

                    int confirm = JOptionPane.showConfirmDialog(ListagemProdutosFrame.this,
                            "Tem certeza que deseja excluir este produto?", "Confirmação", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        ProdutoDAO.excluirProduto(id);

                        // Atualiza a tabela após a exclusão
                        atualizarTabela(tableModel);

                        JOptionPane.showMessageDialog(ListagemProdutosFrame.this,
                                "Produto excluído com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(ListagemProdutosFrame.this,
                            "Erro ao excluir o produto: " + ex.getMessage(),
                            "Erro", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(ListagemProdutosFrame.this,
                        "Por favor, selecione um produto para excluir.", "Erro", JOptionPane.WARNING_MESSAGE);
            }
        });

        // Ação do botão "Fechar"
        btnFechar.addActionListener(e -> dispose());

        // Adicionar os botões ao layout
        JPanel panelButtons = new JPanel();
        panelButtons.setLayout(new GridLayout(1, 3, 10, 10)); // Configurar o layout para 3 botões
        panelButtons.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panelButtons.add(btnAtualizar);
        panelButtons.add(btnExcluir);
        panelButtons.add(btnFechar);
        add(panelButtons, BorderLayout.SOUTH);

        setVisible(true); // Exibe a janela
    }

    // Método para recarregar os dados da tabela
    private void atualizarTabela(DefaultTableModel tableModel) {
        tableModel.setRowCount(0); // Limpa os dados da tabela
        List<Produto> produtos = ProdutoDAO.listarProdutos(); // Recarrega os produtos do banco

        for (Produto p : produtos) {
            Object[] rowData = {p.getId(), p.getNome(), p.getPreco(), p.getQuantidade()};
            tableModel.addRow(rowData); // Adiciona novamente os produtos à tabela
        }
    }
}
