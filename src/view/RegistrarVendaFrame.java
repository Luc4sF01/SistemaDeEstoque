package view;

import dao.ProdutoDAO;
import dao.VendaDAO;
import model.Produto;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class RegistrarVendaFrame extends JFrame {

    private JPanel mainPanel;
    private JPanel productListPanel;
    private JLabel totalLabel;
    private JTextField valorPagoField;
    private JLabel trocoLabel;
    private JButton btnSalvar;
    private JButton btnCancelar;
    private JButton btnMais;
    private JButton btnCalcularTroco;

    private ArrayList<JPanel> produtoLinhas;
    private ProdutoDAO produtoDAO;

    public RegistrarVendaFrame() {
        setTitle("Registrar Venda");
        setSize(800, 600); // Tamanho inicial da janela
        setLocationRelativeTo(null); // Centraliza a janela
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        produtoDAO = new ProdutoDAO();
        produtoLinhas = new ArrayList<>();

        // Painel principal
        mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(new Color(45, 45, 45));

        // Título
        JLabel titleLabel = new JLabel("Registrar Venda", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Painel de lista de produtos
        productListPanel = new JPanel();
        productListPanel.setLayout(new BoxLayout(productListPanel, BoxLayout.Y_AXIS));
        productListPanel.setBackground(new Color(45, 45, 45));

        JScrollPane scrollPane = new JScrollPane(productListPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Painel inferior para total, valor pago e troco
        JPanel bottomPanel = new JPanel(new GridBagLayout());
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        bottomPanel.setBackground(new Color(45, 45, 45));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Valor Total
        gbc.gridx = 0;
        gbc.gridy = 0;
        bottomPanel.add(new JLabel("Valor Total:", SwingConstants.RIGHT), gbc);
        totalLabel = new JLabel("R$ 0.00");
        totalLabel.setFont(new Font("Arial", Font.BOLD, 18));
        totalLabel.setForeground(Color.WHITE);
        gbc.gridx = 1;
        bottomPanel.add(totalLabel, gbc);

        // Valor Pago
        gbc.gridx = 0;
        gbc.gridy = 1;
        bottomPanel.add(new JLabel("Valor Pago:", SwingConstants.RIGHT), gbc);
        valorPagoField = new JTextField();
        valorPagoField.setFont(new Font("Arial", Font.PLAIN, 16));
        valorPagoField.setHorizontalAlignment(JTextField.RIGHT);
        gbc.gridx = 1;
        bottomPanel.add(valorPagoField, gbc);

        // Botão para calcular o troco
        gbc.gridx = 0;
        gbc.gridy = 2;
        bottomPanel.add(new JLabel("Troco:", SwingConstants.RIGHT), gbc);
        trocoLabel = new JLabel("R$ 0.00");
        trocoLabel.setFont(new Font("Arial", Font.BOLD, 18));
        trocoLabel.setForeground(Color.WHITE);
        gbc.gridx = 1;
        bottomPanel.add(trocoLabel, gbc);

        btnCalcularTroco = new JButton("Calcular Troco");
        btnCalcularTroco.setFont(new Font("Arial", Font.PLAIN, 16));
        btnCalcularTroco.setBackground(new Color(70, 73, 75));
        btnCalcularTroco.setForeground(Color.WHITE);
        btnCalcularTroco.setFocusPainted(false);
        btnCalcularTroco.addActionListener(e -> calcularTroco());
        gbc.gridx = 1;
        gbc.gridy = 3;
        bottomPanel.add(btnCalcularTroco, gbc);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        // Botões de ação
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        actionPanel.setBackground(new Color(45, 45, 45));

        btnMais = new JButton("+");
        btnMais.setFont(new Font("Arial", Font.PLAIN, 16));
        btnMais.setBackground(new Color(70, 73, 75));
        btnMais.setForeground(Color.WHITE);
        btnMais.setFocusPainted(false);
        btnMais.addActionListener(e -> adicionarLinhaProduto());

        btnSalvar = new JButton("Salvar");
        btnSalvar.setFont(new Font("Arial", Font.PLAIN, 16));
        btnSalvar.setBackground(new Color(70, 73, 75));
        btnSalvar.setForeground(Color.WHITE);
        btnSalvar.setFocusPainted(false);
        btnSalvar.addActionListener(e -> salvarVenda());

        btnCancelar = new JButton("Cancelar");
        btnCancelar.setFont(new Font("Arial", Font.PLAIN, 16));
        btnCancelar.setBackground(new Color(70, 73, 75));
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setFocusPainted(false);
        btnCancelar.addActionListener(e -> dispose());

        actionPanel.add(btnMais);
        actionPanel.add(btnSalvar);
        actionPanel.add(btnCancelar);

        mainPanel.add(actionPanel, BorderLayout.NORTH);

        add(mainPanel);
        setVisible(true);

        adicionarLinhaProduto();
    }

    private void adicionarLinhaProduto() {
        JPanel produtoLinha = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        produtoLinha.setBackground(new Color(45, 45, 45));

        JComboBox<String> comboProdutos = new JComboBox<>();
        for (Produto produto : produtoDAO.listarProdutos()) {
            comboProdutos.addItem(produto.getNome());
        }
        comboProdutos.setFont(new Font("Arial", Font.PLAIN, 16));
        comboProdutos.setPreferredSize(new Dimension(200, 30));

        JTextField quantidadeField = new JTextField();
        quantidadeField.setFont(new Font("Arial", Font.PLAIN, 16));
        quantidadeField.setPreferredSize(new Dimension(80, 30));
        quantidadeField.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent e) {
                calcularTotal();
            }
        });

        JButton btnMenos = new JButton("-");
        btnMenos.setFont(new Font("Arial", Font.BOLD, 16));
        btnMenos.setPreferredSize(new Dimension(50, 30));
        btnMenos.setBackground(new Color(70, 73, 75));
        btnMenos.setForeground(Color.WHITE);
        btnMenos.addActionListener(e -> removerLinhaProduto(produtoLinha));

        produtoLinha.add(comboProdutos);
        produtoLinha.add(quantidadeField);
        produtoLinha.add(btnMenos);

        produtoLinhas.add(produtoLinha);
        productListPanel.add(produtoLinha);
        productListPanel.revalidate();
        productListPanel.repaint();
    }

    private void removerLinhaProduto(JPanel produtoLinha) {
        produtoLinhas.remove(produtoLinha);
        productListPanel.remove(produtoLinha);
        productListPanel.revalidate();
        productListPanel.repaint();
        calcularTotal();
    }

    private void calcularTotal() {
        double total = 0.0;
        for (JPanel produtoLinha : produtoLinhas) {
            JComboBox<String> comboProdutos = (JComboBox<String>) produtoLinha.getComponent(0);
            JTextField quantidadeField = (JTextField) produtoLinha.getComponent(1);

            String produtoNome = (String) comboProdutos.getSelectedItem();
            String quantidadeTexto = quantidadeField.getText();

            if (produtoNome != null && quantidadeTexto.matches("\\d+")) {
                Produto produto = produtoDAO.buscarPorNome(produtoNome);
                if (produto != null) {
                    int quantidade = Integer.parseInt(quantidadeTexto);
                    total += produto.getPreco() * quantidade;
                }
            }
        }
        totalLabel.setText(String.format("R$ %.2f", total));
    }

    private void calcularTroco() {
        try {
            double total = Double.parseDouble(totalLabel.getText().replace("R$", "").replace(",", ".").trim());
            double valorPago = Double.parseDouble(valorPagoField.getText().trim().replace(",", "."));
            double troco = valorPago - total;

            if (troco >= 0) {
                trocoLabel.setText(String.format("R$ %.2f", troco));
            } else {
                trocoLabel.setText("R$ 0.00");
            }
        } catch (NumberFormatException e) {
            trocoLabel.setText("R$ 0.00");
        }
    }

    private void salvarVenda() {
        if (produtoLinhas.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nenhum produto foi adicionado à venda!", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        calcularTotal();

        int confirm = JOptionPane.showConfirmDialog(this, "Deseja confirmar a venda?", "Confirmação", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            for (JPanel produtoLinha : produtoLinhas) {
                JComboBox<String> comboProdutos = (JComboBox<String>) produtoLinha.getComponent(0);
                JTextField quantidadeField = (JTextField) produtoLinha.getComponent(1);

                String produtoNome = (String) comboProdutos.getSelectedItem();
                String quantidadeTexto = quantidadeField.getText();

                if (produtoNome != null && quantidadeTexto.matches("\\d+")) {
                    Produto produto = produtoDAO.buscarPorNome(produtoNome);
                    if (produto != null) {
                        int quantidade = Integer.parseInt(quantidadeTexto);
                        double valorTotal = produto.getPreco() * quantidade;

                        VendaDAO.registrarVenda(produto.getId(), produtoNome, quantidade, valorTotal, LocalDate.now());
                    }
                }
            }
            JOptionPane.showMessageDialog(this, "Venda registrada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        }
    }
}