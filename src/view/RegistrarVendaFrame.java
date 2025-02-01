package view;

import dao.ProdutoDAO;
import model.Produto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class RegistrarVendaFrame extends JFrame {

    private DefaultTableModel tableModel;
    private List<Produto> produtosDisponiveis;
    private List<Produto> produtosSelecionados;
    private JLabel lblValorTotal;

    public RegistrarVendaFrame() {
        produtosDisponiveis = ProdutoDAO.listarProdutos();
        produtosSelecionados = new ArrayList<>();

        setTitle("Registrar Venda - Seleção de Produtos");
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Abre a tela maximizada
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.decode("#F9FAFB"));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridwidth = GridBagConstraints.REMAINDER;

        // Título
        JLabel titleLabel = new JLabel("Registrar Venda - Seleção de Produtos", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 30));
        titleLabel.setForeground(Color.decode("#4A4E69"));
        panel.add(titleLabel, gbc);

        // Campo de produto com autocomplete
        JLabel lblProduto = new JLabel("Produto:");
        lblProduto.setFont(new Font("SansSerif", Font.BOLD, 18));
        panel.add(lblProduto, gbc);

        JComboBox<String> cbProduto = new JComboBox<>();
        cbProduto.setEditable(true);
        carregarProdutos(cbProduto);
        cbProduto.setFont(new Font("SansSerif", Font.PLAIN, 16));
        cbProduto.setPreferredSize(new Dimension(500, 30));
        panel.add(cbProduto, gbc);

        // Campo de quantidade
        JLabel lblQuantidade = new JLabel("Quantidade:");
        lblQuantidade.setFont(new Font("SansSerif", Font.BOLD, 18));
        panel.add(lblQuantidade, gbc);

        JTextField txtQuantidade = new JTextField();
        txtQuantidade.setFont(new Font("SansSerif", Font.PLAIN, 16));
        txtQuantidade.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.decode("#CCCCCC"), 1, true),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        txtQuantidade.setPreferredSize(new Dimension(500, 30));
        panel.add(txtQuantidade, gbc);

        // Tabela para produtos adicionados
        JLabel lblTabela = new JLabel("Produtos Adicionados:");
        lblTabela.setFont(new Font("SansSerif", Font.BOLD, 18));
        panel.add(lblTabela, gbc);

        String[] colunas = {"Produto", "Preço Unitário", "Quantidade", "Subtotal"};
        tableModel = new DefaultTableModel(colunas, 0);
        JTable tabelaProdutos = new JTable(tableModel);
        tabelaProdutos.setRowHeight(30);
        JScrollPane scrollPane = new JScrollPane(tabelaProdutos);
        scrollPane.setPreferredSize(new Dimension(1000, 500)); // Aumenta a altura mínima da lista
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.decode("#E5E5E5")));
        panel.add(scrollPane, gbc);

        // Valor total
        lblValorTotal = new JLabel("Valor Total: R$ 0,00");
        lblValorTotal.setFont(new Font("SansSerif", Font.BOLD, 22));
        lblValorTotal.setForeground(Color.decode("#1D3557"));
        panel.add(lblValorTotal, gbc);

        // Botões
        JButton btnAdicionar = criarBotao("Adicionar Produto", "#457B9D", "#1D3557");
        JButton btnRemover = criarBotao("Remover Produto", "#E63946", "#C0392B");
        JButton btnProximo = criarBotao("Próximo", "#457B9D", "#1D3557");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(btnAdicionar);
        buttonPanel.add(btnRemover);
        buttonPanel.add(btnProximo);
        buttonPanel.setBackground(Color.decode("#F9FAFB"));
        panel.add(buttonPanel, gbc);

        // Ação do botão Adicionar
        btnAdicionar.addActionListener(e -> {
            try {
                String produtoSelecionado = (String) cbProduto.getSelectedItem();
                Produto produto = produtosDisponiveis.stream()
                        .filter(p -> p.getNome().equals(produtoSelecionado))
                        .findFirst()
                        .orElse(null);

                if (produto == null) {
                    JOptionPane.showMessageDialog(this, "Produto não encontrado!", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int quantidade = Integer.parseInt(txtQuantidade.getText().trim());
                if (quantidade <= 0) {
                    JOptionPane.showMessageDialog(this, "A quantidade deve ser maior que zero!", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                double subtotal = produto.getPreco() * quantidade;
                tableModel.addRow(new Object[]{produto.getNome(), String.format("R$ %.2f", produto.getPreco()), quantidade, String.format("R$ %.2f", subtotal)});

                produtosSelecionados.add(produto);
                produtosDisponiveis.remove(produto);
                cbProduto.removeItem(produto.getNome());
                txtQuantidade.setText("");

                atualizarValorTotal();

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Digite uma quantidade válida!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Ação do botão Remover
        btnRemover.addActionListener(e -> {
            int selectedRow = tabelaProdutos.getSelectedRow();
            if (selectedRow != -1) {
                String nomeProduto = (String) tableModel.getValueAt(selectedRow, 0);
                Produto produto = produtosSelecionados.stream()
                        .filter(p -> p.getNome().equals(nomeProduto))
                        .findFirst()
                        .orElse(null);

                if (produto != null) {
                    produtosDisponiveis.add(produto);
                    cbProduto.addItem(produto.getNome());
                    produtosSelecionados.remove(produto);
                }

                tableModel.removeRow(selectedRow);
                atualizarValorTotal();
            } else {
                JOptionPane.showMessageDialog(this, "Selecione um produto para remover!", "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        });

        // Ação do botão Próximo
        btnProximo.addActionListener(e -> {
            if (produtosSelecionados.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Adicione pelo menos um produto para continuar!", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            new MetodoPagamentoFrame(produtosSelecionados);
            dispose();
        });

        add(panel);
        setVisible(true);
    }

    private void carregarProdutos(JComboBox<String> cbProduto) {
        for (Produto produto : produtosDisponiveis) {
            cbProduto.addItem(produto.getNome());
        }
    }

    private void atualizarValorTotal() {
        double total = 0;
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            String subtotalStr = tableModel.getValueAt(i, 3).toString().replace("R$", "").replace(",", ".").trim();
            total += Double.parseDouble(subtotalStr);
        }
        lblValorTotal.setText(String.format("Valor Total: R$ %.2f", total));
    }

    private JButton criarBotao(String texto, String corPrincipal, String corHover) {
        JButton botao = new JButton(texto);
        botao.setFont(new Font("SansSerif", Font.BOLD, 16));
        botao.setForeground(Color.WHITE);
        botao.setBackground(Color.decode(corPrincipal));
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
