package view;

import dao.ProdutoDAO;
import model.Produto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.List;

public class ListagemProdutosFrame extends JFrame {

    // 🎨 Paleta de cores personalizada
    private final Color MELON = new Color(255, 188, 181);       // Fundo de botões
    private final Color BURNT_SIENNA = new Color(201, 125, 96); // Hover do botão
    private final Color ALMOND = new Color(242, 229, 215);      // Fundo principal
    private final Color TEXT_COLOR = new Color(51, 51, 51);     // Cinza escuro

    public ListagemProdutosFrame() {
        // Configurações da janela
        setTitle("Listagem de Produtos");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);
        setBackground(ALMOND);

        // Painel principal
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(ALMOND);

        // Título da tabela
        JLabel titleLabel = new JLabel("Produtos Cadastrados", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(TEXT_COLOR);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Filtro de busca
        JTextField txtFiltro = new JTextField();
        txtFiltro.setFont(new Font("Arial", Font.PLAIN, 14));
        txtFiltro.setToolTipText("Digite para filtrar produtos");

        JPanel panelFiltro = new JPanel(new BorderLayout());
        panelFiltro.setBackground(ALMOND);
        panelFiltro.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panelFiltro.add(new JLabel("🔍 Filtrar: "), BorderLayout.WEST);
        panelFiltro.add(txtFiltro, BorderLayout.CENTER);

        mainPanel.add(panelFiltro, BorderLayout.NORTH);

        // Tabela de produtos
        String[] colunas = {"ID", "Nome", "Preço (R$)", "Quantidade"};
        DefaultTableModel tableModel = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable tabela = new JTable(tableModel);
        tabela.setFont(new Font("Arial", Font.PLAIN, 14));
        tabela.setRowHeight(25);
        tabela.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        tabela.getTableHeader().setBackground(MELON);
        tabela.getTableHeader().setForeground(TEXT_COLOR);

        TableRowSorter<DefaultTableModel> rowSorter = new TableRowSorter<>(tableModel);
        tabela.setRowSorter(rowSorter);
        txtFiltro.addCaretListener(e -> {
            String text = txtFiltro.getText();
            rowSorter.setRowFilter(text.trim().isEmpty() ? null : RowFilter.regexFilter("(?i)" + text));
        });

        JScrollPane scrollPane = new JScrollPane(tabela);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Botões de ação
        JButton btnAtualizar = criarBotao("Atualizar Produto");
        JButton btnExcluir = criarBotao("Excluir Produto");
        JButton btnFechar = criarBotao("Fechar");

        // Eventos dos botões
        btnAtualizar.addActionListener(e -> atualizarProduto(tabela, tableModel));
        btnExcluir.addActionListener(e -> excluirProduto(tabela, tableModel));
        btnFechar.addActionListener(e -> dispose());

        JPanel buttonPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        buttonPanel.setBackground(ALMOND);

        buttonPanel.add(btnAtualizar);
        buttonPanel.add(btnExcluir);
        buttonPanel.add(btnFechar);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Adiciona o painel principal à janela
        add(mainPanel);
        atualizarTabela(tableModel);

        setVisible(true);
    }

    // Criação de botões com estilo
    private JButton criarBotao(String texto) {
        JButton botao = new JButton(texto);
        botao.setFont(new Font("Arial", Font.PLAIN, 16));
        botao.setForeground(TEXT_COLOR);
        botao.setBackground(MELON);
        botao.setFocusPainted(false);
        botao.setBorder(BorderFactory.createLineBorder(BURNT_SIENNA));

        // Efeito hover
        botao.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                botao.setBackground(BURNT_SIENNA);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                botao.setBackground(MELON);
            }
        });
        return botao;
    }

    // Atualiza tabela com dados do banco
    private void atualizarTabela(DefaultTableModel tableModel) {
        tableModel.setRowCount(0);
        List<Produto> produtos = ProdutoDAO.listarProdutos();

        for (Produto p : produtos) {
            tableModel.addRow(new Object[]{
                    p.getId(),
                    p.getNome(),
                    String.format("R$ %.2f", p.getPreco()),
                    p.getQuantidade()
            });
        }
    }

    // Método para atualizar produto
    private void atualizarProduto(JTable tabela, DefaultTableModel tableModel) {
        int selectedRow = tabela.getSelectedRow();
        if (selectedRow >= 0) {
            int id = Integer.parseInt(tabela.getValueAt(selectedRow, 0).toString());
            Produto produtoSelecionado = ProdutoDAO.buscarPorId(id);
            if (produtoSelecionado != null) {
                AtualizarProdutoFrame atualizarFrame = new AtualizarProdutoFrame(produtoSelecionado);
                atualizarFrame.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosed(java.awt.event.WindowEvent e) {
                        atualizarTabela(tableModel);
                    }
                });
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um produto para atualizar.", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }

    // Método para excluir produto
    private void excluirProduto(JTable tabela, DefaultTableModel tableModel) {
        int selectedRow = tabela.getSelectedRow();
        if (selectedRow >= 0) {
            int id = Integer.parseInt(tabela.getValueAt(selectedRow, 0).toString());
            int confirm = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja excluir?", "Confirmação", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                ProdutoDAO.excluirProduto(id);
                atualizarTabela(tableModel);
                JOptionPane.showMessageDialog(this, "Produto excluído com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um produto para excluir.", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }
}
