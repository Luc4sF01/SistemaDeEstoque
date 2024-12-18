package view;

import dao.VendaDAO;
import model.Venda;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class RelatorioVendasFrame extends JFrame {

    private JLabel totalLabel; // Novo campo para exibir o total das vendas

    public RelatorioVendasFrame() {
        setTitle("Relatório de Vendas");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);

        JLabel titleLabel = new JLabel("Relatório de Vendas", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        add(titleLabel, BorderLayout.NORTH);

        // Painel de filtros
        JPanel filterPanel = new JPanel(new FlowLayout());
        JLabel lblDataInicio = new JLabel("Data Início (YYYY-MM-DD):");
        JTextField txtDataInicio = new JTextField(10);
        JLabel lblDataFim = new JLabel("Data Fim (YYYY-MM-DD):");
        JTextField txtDataFim = new JTextField(10);
        JButton btnFiltrar = new JButton("Filtrar");

        filterPanel.add(lblDataInicio);
        filterPanel.add(txtDataInicio);
        filterPanel.add(lblDataFim);
        filterPanel.add(txtDataFim);
        filterPanel.add(btnFiltrar);

        add(filterPanel, BorderLayout.NORTH);

        // Tabela de vendas
        String[] colunas = {"ID", "Produto", "Quantidade", "Valor Total", "Data"};
        DefaultTableModel tableModel = new DefaultTableModel(colunas, 0);
        JTable tabela = new JTable(tableModel);
        add(new JScrollPane(tabela), BorderLayout.CENTER);

        // Painel inferior com total e botão fechar
        JPanel bottomPanel = new JPanel(new BorderLayout());
        totalLabel = new JLabel("Total: R$ 0.00", SwingConstants.RIGHT);
        totalLabel.setFont(new Font("Arial", Font.BOLD, 16));
        bottomPanel.add(totalLabel, BorderLayout.CENTER);

        JButton btnFechar = new JButton("Fechar");
        bottomPanel.add(btnFechar, BorderLayout.EAST);
        add(bottomPanel, BorderLayout.SOUTH);

        // Ação do botão Fechar
        btnFechar.addActionListener(e -> dispose());

        // Ação do botão Filtrar
        btnFiltrar.addActionListener(e -> {
            String dataInicio = txtDataInicio.getText();
            String dataFim = txtDataFim.getText();

            if (dataInicio.isEmpty() || dataFim.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor, preencha ambas as datas.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                LocalDate inicio = LocalDate.parse(dataInicio);
                LocalDate fim = LocalDate.parse(dataFim);

                if (inicio.isAfter(fim)) {
                    JOptionPane.showMessageDialog(this, "A data de início não pode ser maior que a data de fim.", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                List<Venda> vendas = VendaDAO.buscarVendasPorPeriodo(inicio, fim);
                atualizarTabela(tableModel, vendas);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao filtrar vendas: Verifique o formato das datas (YYYY-MM-DD).", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        setVisible(true);
    }

    // Atualiza a tabela e calcula o total das vendas
    private void atualizarTabela(DefaultTableModel tableModel, List<Venda> vendas) {
        tableModel.setRowCount(0);
        double totalVendas = 0;

        for (Venda venda : vendas) {
            tableModel.addRow(new Object[]{
                    venda.getId(),
                    venda.getProdutoNome(),
                    venda.getQuantidade(),
                    venda.getValorTotal(),
                    venda.getDataVenda()
            });
            totalVendas += venda.getValorTotal(); // Soma o valor total
        }

        // Atualiza o JLabel com o total
        totalLabel.setText(String.format("Total: R$ %.2f", totalVendas));
    }
}
