package view;

import dao.VendaDAO;
import model.Venda;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class RelatorioVendasFrame extends JFrame {

    public RelatorioVendasFrame() {
        setTitle("Relatório de Vendas");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);

        JLabel titleLabel = new JLabel("Relatório de Vendas", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        add(titleLabel, BorderLayout.NORTH);

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

        String[] colunas = {"ID", "Produto", "Quantidade", "Valor Total", "Data"};
        DefaultTableModel tableModel = new DefaultTableModel(colunas, 0);
        JTable tabela = new JTable(tableModel);
        add(new JScrollPane(tabela), BorderLayout.CENTER);

        JButton btnFechar = new JButton("Fechar");
        JPanel bottomPanel = new JPanel(new FlowLayout());
        bottomPanel.add(btnFechar);
        add(bottomPanel, BorderLayout.SOUTH);

        btnFechar.addActionListener(e -> dispose());

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

    private void atualizarTabela(DefaultTableModel tableModel, List<Venda> vendas) {
        tableModel.setRowCount(0);

        for (Venda venda : vendas) {
            tableModel.addRow(new Object[]{
                    venda.getId(),
                    venda.getProdutoNome(),
                    venda.getQuantidade(),
                    venda.getValorTotal(),
                    venda.getDataVenda()
            });
        }
    }
}