package view;

import dao.ProdutoDAO;
import model.Produto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.List;

public class ListagemProdutosFrame extends JFrame {

    private JTable tabela;
    private DefaultTableModel tableModel;
    private TableRowSorter<DefaultTableModel> rowSorter;
    private JTextField txtFiltro;

    public ListagemProdutosFrame() {
        setTitle("Listagem de Produtos");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Criando painel principal
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.decode("#F7F7F7"));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        // Título estilizado
        JLabel titleLabel = new JLabel("Produtos Cadastrados", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.decode("#5A47AB"));
        panel.add(titleLabel, BorderLayout.NORTH);

        // Painel do filtro de busca
        JPanel filterPanel = new JPanel(new BorderLayout());
        filterPanel.setBackground(Color.decode("#F7F7F7"));

        txtFiltro = new JTextField();
        txtFiltro.setFont(new Font("SansSerif", Font.PLAIN, 16));
        txtFiltro.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.decode("#CCCCCC"), 1, true),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        txtFiltro.setBackground(Color.WHITE);
        txtFiltro.setToolTipText("Digite para filtrar produtos");

        JLabel lblFiltro = new JLabel("🔍 ");
        lblFiltro.setFont(new Font("SansSerif", Font.BOLD, 16));

        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.setBackground(Color.WHITE);
        inputPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.decode("#CCCCCC"), 1, true),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        inputPanel.add(lblFiltro, BorderLayout.WEST);
        inputPanel.add(txtFiltro, BorderLayout.CENTER);

        filterPanel.add(inputPanel, BorderLayout.CENTER);
        panel.add(filterPanel, BorderLayout.NORTH);

        // Criando tabela de produtos
        String[] colunas = {"ID", "Nome", "Preço (R$)", "Quantidade", "Editar"};
        tableModel = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4; // Apenas a coluna "Editar" será clicável
            }
        };

        tabela = new JTable(tableModel);
        tabela.setFont(new Font("SansSerif", Font.PLAIN, 14));
        tabela.setRowHeight(30);
        tabela.setGridColor(Color.decode("#E0E0E0")); // Linhas mais leves
        tabela.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));
        tabela.getTableHeader().setBackground(Color.decode("#5A47AB"));
        tabela.getTableHeader().setForeground(Color.WHITE);

        // Fundo mais claro
        tabela.setBackground(Color.decode("#FFFFFF"));
        tabela.setForeground(Color.decode("#333333")); // Texto mais escuro
        tabela.setSelectionBackground(Color.decode("#D5D5FF")); // Fundo da seleção
        tabela.setSelectionForeground(Color.decode("#000000")); // Texto da seleção

        tabela.getColumn("Editar").setCellRenderer(new ButtonRenderer());
        tabela.getColumn("Editar").setCellEditor(new ButtonEditor(new JCheckBox()));

        rowSorter = new TableRowSorter<>(tableModel);
        tabela.setRowSorter(rowSorter);

        txtFiltro.addCaretListener(e -> {
            String text = txtFiltro.getText();
            rowSorter.setRowFilter(text.trim().isEmpty() ? null : RowFilter.regexFilter("(?i)" + text));
        });

        // Painel da tabela com sombra leve
        JScrollPane scrollPane = new JScrollPane(tabela);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.decode("#CCCCCC"), 1, true),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        panel.add(scrollPane, BorderLayout.CENTER);

        // Botões de ação
        JButton btnExcluir = criarBotao("Excluir Produto", "#E74C3C", "#C0392B");
        JButton btnFechar = criarBotao("Fechar", "#5A47AB", "#483C9B");

        btnExcluir.addActionListener(e -> excluirProduto());
        btnFechar.addActionListener(e -> dispose());

        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setBackground(Color.decode("#F7F7F7"));
        buttonPanel.add(btnExcluir);
        buttonPanel.add(Box.createHorizontalStrut(20));
        buttonPanel.add(btnFechar);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        add(panel);
        atualizarTabela();
        setVisible(true);
    }

    // Atualiza a tabela com os produtos cadastrados
    private void atualizarTabela() {
        tableModel.setRowCount(0);
        List<Produto> produtos = ProdutoDAO.listarProdutos();
        for (Produto p : produtos) {
            tableModel.addRow(new Object[]{p.getId(), p.getNome(), String.format("R$ %.2f", p.getPreco()), p.getQuantidade(), "✏️ Editar"});
        }
    }

    // Método para excluir produto
    private void excluirProduto() {
        int selectedRow = tabela.getSelectedRow();
        if (selectedRow >= 0) {
            int id = Integer.parseInt(tabela.getValueAt(selectedRow, 0).toString());
            int confirm = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja excluir?", "Confirmação", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                ProdutoDAO.excluirProduto(id);
                atualizarTabela();
                JOptionPane.showMessageDialog(this, "Produto excluído com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um produto para excluir.", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }

    // Método para criar botões estilizados
    private JButton criarBotao(String texto, String corPrincipal, String corHover) {
        JButton botao = new JButton(texto);
        botao.setFont(new Font("SansSerif", Font.BOLD, 16));
        botao.setForeground(Color.WHITE);
        botao.setBackground(Color.decode(corPrincipal));
        botao.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        botao.setFocusPainted(false);
        botao.setCursor(new Cursor(Cursor.HAND_CURSOR));

        botao.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                botao.setBackground(Color.decode(corHover));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                botao.setBackground(Color.decode(corPrincipal));
            }
        });

        return botao;
    }
}
