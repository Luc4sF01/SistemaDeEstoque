package view;

import dao.ProdutoDAO;
import model.Produto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.List;

public class ListagemProdutosFrame extends JFrame {

    public ListagemProdutosFrame() {
        // Configurações da janela
        setTitle("Listagem de Produtos");
        setSize(800, 600);
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
                return false; // Impede edição das células
            }
        };
        JTable tabela = new JTable(tableModel);
        tabela.setFont(new Font("Arial", Font.PLAIN, 14));
        tabela.setRowHeight(25);
        tabela.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Adicionar filtro de busca
        TableRowSorter<DefaultTableModel> rowSorter = new TableRowSorter<>(tableModel);
        tabela.setRowSorter(rowSorter);
        JTextField txtFiltro = new JTextField();
        txtFiltro.setFont(new Font("Arial", Font.PLAIN, 14));
        txtFiltro.setToolTipText("Digite para filtrar produtos");
        txtFiltro.addCaretListener(e -> {
            String text = txtFiltro.getText();
            if (text.trim().isEmpty()) {
                rowSorter.setRowFilter(null);
            } else {
                rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
            }
        });

        JPanel panelFiltro = new JPanel(new BorderLayout());
        panelFiltro.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panelFiltro.add(new JLabel("🔍 Filtrar: "), BorderLayout.WEST);
        panelFiltro.add(txtFiltro, BorderLayout.CENTER);
        add(panelFiltro, BorderLayout.NORTH);

        // Atualizar tabela com dados do banco
        atualizarTabela(tableModel);

        // Adicionar tabela a um painel com rolagem
        JScrollPane scrollPane = new JScrollPane(tabela);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(scrollPane, BorderLayout.CENTER);

        // Botões de ação
        JButton btnAtualizar = new JButton("Atualizar Produto");
        JButton btnExcluir = new JButton("Excluir Produto");
        JButton btnFechar = new JButton("Fechar");

        // Ação do botão "Atualizar Produto"
        btnAtualizar.addActionListener(e -> {
            int selectedRow = tabela.getSelectedRow();
            if (selectedRow >= 0) {
                try {
                    int id = Integer.parseInt(tabela.getValueAt(selectedRow, 0).toString());
                    Produto produtoSelecionado = ProdutoDAO.buscarPorId(id);

                    if (produtoSelecionado != null) {
                        AtualizarProdutoFrame atualizarFrame = new AtualizarProdutoFrame(produtoSelecionado);

                        // Recarregar tabela após atualização
                        atualizarFrame.addWindowListener(new java.awt.event.WindowAdapter() {
                            @Override
                            public void windowClosed(java.awt.event.WindowEvent e) {
                                atualizarTabela(tableModel);
                            }
                        });
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Erro ao atualizar: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Selecione um produto para atualizar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        });

        // Ação do botão "Excluir Produto"
        btnExcluir.addActionListener(e -> {
            int selectedRow = tabela.getSelectedRow();
            if (selectedRow >= 0) {
                try {
                    int id = Integer.parseInt(tabela.getValueAt(selectedRow, 0).toString());
                    int confirm = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja excluir?", "Confirmação", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        ProdutoDAO.excluirProduto(id);
                        atualizarTabela(tableModel);
                        JOptionPane.showMessageDialog(this, "Produto excluído com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Erro ao excluir: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Selecione um produto para excluir.", "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        });

        // Ação do botão "Fechar"
        btnFechar.addActionListener(e -> dispose());

        // Painel dos botões
        JPanel panelButtons = new JPanel();
        panelButtons.setLayout(new GridLayout(1, 3, 10, 10));
        panelButtons.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panelButtons.add(btnAtualizar);
        panelButtons.add(btnExcluir);
        panelButtons.add(btnFechar);
        add(panelButtons, BorderLayout.SOUTH);

        setVisible(true);
    }

    // Atualizar dados na tabela
    private void atualizarTabela(DefaultTableModel tableModel) {
        tableModel.setRowCount(0); // Limpa os dados da tabela
        List<Produto> produtos = ProdutoDAO.listarProdutos(); // Recarrega os produtos do banco

        for (Produto p : produtos) {
            Object[] rowData = {p.getId(), p.getNome(), String.format("R$ %.2f", p.getPreco()), p.getQuantidade()};
            tableModel.addRow(rowData);
        }
    }
}
