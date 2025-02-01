package view;

import dao.VendaDAO;
import model.Produto;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class MetodoPagamentoFrame extends JFrame {

    private JLabel lblTotal, lblTroco;
    private JTextField txtPago;
    private double total;

    public MetodoPagamentoFrame(List<Produto> produtosSelecionados) {
        setTitle("Registrar Venda - Método de Pagamento");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Calcular o total da venda
        total = produtosSelecionados.stream()
                .mapToDouble(produto -> produto.getPreco() * 1) // Supondo quantidade = 1
                .sum();

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.decode("#F9FAFB"));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridwidth = GridBagConstraints.REMAINDER;

        // Título
        JLabel titleLabel = new JLabel("Registrar Venda - Método de Pagamento", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 30));
        titleLabel.setForeground(Color.decode("#4A4E69"));
        panel.add(titleLabel, gbc);

        // Valor total
        lblTotal = new JLabel(String.format("Total (R$): R$ %.2f", total));
        lblTotal.setFont(new Font("SansSerif", Font.BOLD, 22));
        lblTotal.setForeground(Color.decode("#1D3557"));
        panel.add(lblTotal, gbc);

        // Método de pagamento
        JLabel lblPagamento = new JLabel("Método de Pagamento:");
        lblPagamento.setFont(new Font("SansSerif", Font.BOLD, 18));
        panel.add(lblPagamento, gbc);

        JComboBox<String> cbPagamento = new JComboBox<>(new String[]{"Dinheiro", "Pix", "Crédito", "Débito"});
        cbPagamento.setFont(new Font("SansSerif", Font.PLAIN, 16));
        cbPagamento.setPreferredSize(new Dimension(400, 30));
        panel.add(cbPagamento, gbc);

        // Campo de valor pago
        JLabel lblPago = new JLabel("Valor Pago:");
        lblPago.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblPago.setVisible(false);
        txtPago = new JTextField(20);
        txtPago.setFont(new Font("SansSerif", Font.PLAIN, 16));
        txtPago.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.decode("#CCCCCC"), 1, true),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        txtPago.setVisible(false);
        panel.add(lblPago, gbc);
        panel.add(txtPago, gbc);

        // Troco
        lblTroco = new JLabel("Troco (R$): R$ 0,00");
        lblTroco.setFont(new Font("SansSerif", Font.BOLD, 20));
        lblTroco.setForeground(Color.decode("#E63946"));
        lblTroco.setVisible(false);
        panel.add(lblTroco, gbc);

        // Mostrar/esconder campos para dinheiro
        cbPagamento.addActionListener(e -> {
            boolean isDinheiro = cbPagamento.getSelectedItem().equals("Dinheiro");
            lblPago.setVisible(isDinheiro);
            txtPago.setVisible(isDinheiro);
            lblTroco.setVisible(isDinheiro);
        });

        // Botão para calcular troco
        JButton btnCalcularTroco = criarBotao("Calcular Troco", "#457B9D", "#1D3557");
        btnCalcularTroco.setVisible(false);
        btnCalcularTroco.addActionListener(e -> {
            try {
                double valorPago = Double.parseDouble(txtPago.getText().trim());
                double troco = valorPago - total;
                lblTroco.setText(String.format("Troco (R$): R$ %.2f", troco > 0 ? troco : 0));
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Digite um valor válido!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Mostrar/esconder botão de calcular troco
        cbPagamento.addActionListener(e -> btnCalcularTroco.setVisible(cbPagamento.getSelectedItem().equals("Dinheiro")));
        panel.add(btnCalcularTroco, gbc);

        // Botões
        JButton btnVoltar = criarBotao("Voltar", "#3498DB", "#2980B9");
        JButton btnRegistrar = criarBotao("Registrar Venda", "#457B9D", "#1D3557");
        JButton btnCancelar = criarBotao("Cancelar", "#E63946", "#C0392B");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(btnVoltar);
        buttonPanel.add(btnRegistrar);
        buttonPanel.add(btnCancelar);
        buttonPanel.setBackground(Color.decode("#F9FAFB"));
        panel.add(buttonPanel, gbc);

        // Ação do botão Voltar
        btnVoltar.addActionListener(e -> {
            new RegistrarVendaFrame(); // Retorna à tela de registro de produtos
            dispose();
        });

        // Ação do botão Registrar
        btnRegistrar.addActionListener(e -> {
            try {
                LocalDate dataVenda = LocalDate.now();
                String dataVendaStr = dataVenda.toString(); // Conversão corrigida

                for (Produto produto : produtosSelecionados) {
                    VendaDAO.registrarVenda(produto.getId(), produto.getNome(), 1, produto.getPreco(), dataVendaStr);
                }

                JOptionPane.showMessageDialog(this, "Venda registrada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao registrar venda!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Ação do botão Cancelar
        btnCancelar.addActionListener(e -> dispose());

        add(panel);
        setVisible(true);
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
