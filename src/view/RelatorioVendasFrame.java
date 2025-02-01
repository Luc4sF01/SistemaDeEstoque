package view;

import dao.VendaDAO;
import model.Venda;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

public class RelatorioVendasFrame extends JFrame {

    private DefaultTableModel tableModel;
    private JLabel lblTotalVendas;
    private JTextField txtDataInicio, txtDataFim;
    private SimpleDateFormat formatoEntrada = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat formatoSaida = new SimpleDateFormat("dd/MM/yyyy");

    public RelatorioVendasFrame() {
        setTitle("Relatório de Vendas");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Utilizando java.awt.Color sem ambiguidade
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(java.awt.Color.decode("#F9FAFB"));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridwidth = GridBagConstraints.REMAINDER;

        // Título estilizado
        JLabel titleLabel = new JLabel("Relatório de Vendas", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 30));
        titleLabel.setForeground(java.awt.Color.decode("#4A4E69"));
        panel.add(titleLabel, gbc);

        // Painel de filtro de data
        JPanel filtroPanel = new JPanel();
        filtroPanel.setBackground(java.awt.Color.decode("#F9FAFB"));

        JLabel lblDataInicio = new JLabel("Data Início (DD/MM/AAAA):");
        lblDataInicio.setFont(new Font("SansSerif", Font.BOLD, 16));
        filtroPanel.add(lblDataInicio);

        txtDataInicio = new JTextField(10);
        filtroPanel.add(txtDataInicio);

        JLabel lblDataFim = new JLabel("Data Fim (DD/MM/AAAA):");
        lblDataFim.setFont(new Font("SansSerif", Font.BOLD, 16));
        filtroPanel.add(lblDataFim);

        txtDataFim = new JTextField(10);
        filtroPanel.add(txtDataFim);

        JButton btnFiltrar = criarBotao("Filtrar", "#457B9D", "#1D3557");
        filtroPanel.add(btnFiltrar);

        panel.add(filtroPanel, gbc);

        // Tabela estilizada para exibição das vendas
        String[] colunas = {"ID", "Produto", "Quantidade", "Total (R$)", "Data"};
        tableModel = new DefaultTableModel(colunas, 0);
        JTable tabelaVendas = new JTable(tableModel);
        tabelaVendas.setRowHeight(30);
        tabelaVendas.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 16));
        tabelaVendas.setFont(new Font("SansSerif", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(tabelaVendas);
        scrollPane.setPreferredSize(new Dimension(1000, 500));
        panel.add(scrollPane, gbc);

        // Totalizador de vendas
        lblTotalVendas = new JLabel("Total de Vendas: R$ 0,00");
        lblTotalVendas.setFont(new Font("SansSerif", Font.BOLD, 20));
        lblTotalVendas.setForeground(java.awt.Color.decode("#1D3557"));
        panel.add(lblTotalVendas, gbc);

        // Botões de exportação e fechamento
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(java.awt.Color.decode("#F9FAFB"));

        JButton btnExportar = criarBotao("Exportar para Excel", "#2E7D32", "#1B5E20");
        JButton btnFechar = criarBotao("Fechar", "#E63946", "#C0392B");

        buttonPanel.add(btnExportar);
        buttonPanel.add(btnFechar);
        panel.add(buttonPanel, gbc);

        // Ação do botão Filtrar
        btnFiltrar.addActionListener(e -> carregarVendas());

        // Ação do botão Exportar para Excel
        btnExportar.addActionListener(e -> exportarParaExcel());

        // Ação do botão Fechar
        btnFechar.addActionListener(e -> dispose());

        add(panel);
        setVisible(true);

        // Carregar todas as vendas no início
        carregarVendas();
    }

    private void carregarVendas() {
        tableModel.setRowCount(0);

        String dataInicio = txtDataInicio.getText().trim();
        String dataFim = txtDataFim.getText().trim();

        try {
            if (!dataInicio.isEmpty()) {
                dataInicio = formatarDataParaBanco(dataInicio);
            }
            if (!dataFim.isEmpty()) {
                dataFim = formatarDataParaBanco(dataFim);
            }
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(this, "Formato de data inválido! Use DD/MM/AAAA.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        List<Venda> vendas = (dataInicio.isEmpty() || dataFim.isEmpty()) ?
                VendaDAO.listarVendas() : VendaDAO.listarVendasPorData(dataInicio, dataFim);

        double totalGeral = 0;

        for (Venda venda : vendas) {
            String dataFormatada = formatarDataParaExibir(venda.getData());

            tableModel.addRow(new Object[]{
                    venda.getId(),
                    venda.getProdutoNome(),
                    venda.getQuantidade(),
                    String.format("R$ %.2f", venda.getTotal()),
                    dataFormatada
            });

            totalGeral += venda.getTotal();
        }

        lblTotalVendas.setText(String.format("Total de Vendas: R$ %.2f", totalGeral));
    }

    private String formatarDataParaBanco(String data) throws ParseException {
        return formatoEntrada.format(formatoSaida.parse(data));
    }

    private String formatarDataParaExibir(String data) {
        try {
            return formatoSaida.format(formatoEntrada.parse(data));
        } catch (ParseException e) {
            return data;
        }
    }

    private JButton criarBotao(String texto, String corPrincipal, String corHover) {
        JButton botao = new JButton(texto);
        botao.setFont(new Font("SansSerif", Font.BOLD, 16));
        botao.setForeground(java.awt.Color.WHITE);
        botao.setBackground(java.awt.Color.decode(corPrincipal));
        botao.setFocusPainted(false);
        botao.setCursor(new Cursor(Cursor.HAND_CURSOR));

        botao.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent evt) {
                botao.setBackground(java.awt.Color.decode(corHover));
            }

            @Override
            public void mouseExited(MouseEvent evt) {
                botao.setBackground(java.awt.Color.decode(corPrincipal));
            }
        });

        return botao;
    }

    private void exportarParaExcel() {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Vendas");

        // Cria a linha de cabeçalho
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < tableModel.getColumnCount(); i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(tableModel.getColumnName(i));
        }

        // Preenche as linhas com os dados da tabela
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            Row row = sheet.createRow(i + 1);
            for (int j = 0; j < tableModel.getColumnCount(); j++) {
                Cell cell = row.createCell(j);
                Object value = tableModel.getValueAt(i, j);
                if (value instanceof Number) {
                    cell.setCellValue(((Number) value).doubleValue());
                } else {
                    cell.setCellValue(value.toString());
                }
            }
        }

        // Ajusta a largura das colunas automaticamente
        for (int i = 0; i < tableModel.getColumnCount(); i++) {
            sheet.autoSizeColumn(i);
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Salvar arquivo Excel");
        int userSelection = fileChooser.showSaveDialog(this);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            String filePath = fileChooser.getSelectedFile().getAbsolutePath();
            if (!filePath.toLowerCase().endsWith(".xlsx")) {
                filePath += ".xlsx";
            }
            try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
                workbook.write(fileOut);
                JOptionPane.showMessageDialog(this, "Arquivo exportado com sucesso!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao exportar arquivo: " + ex.getMessage(),
                        "Erro", JOptionPane.ERROR_MESSAGE);
            } finally {
                try {
                    workbook.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new RelatorioVendasFrame());
    }
}
