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

    // Paleta de cores
    private final Color RAISIN_BLACK = new Color(38, 35, 34);
    private final Color MELON = new Color(255, 188, 181);
    private final Color BURNT_SIENNA = new Color(201, 125, 96);
    private final Color ALMOND = new Color(242, 229, 215);
    private final Color TEXT_COLOR = new Color(51, 51, 51);

    public RegistrarVendaFrame() {
        setTitle("Registrar Venda");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        produtoDAO = new ProdutoDAO();
        produtoLinhas = new ArrayList<>();

        // Painel principal
        mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(ALMOND);

        // Título
        JLabel titleLabel = new JLabel("Registrar Venda", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setForeground(TEXT_COLOR);
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Painel de lista de produtos
        productListPanel = new JPanel();
        productListPanel.setLayout(new BoxLayout(productListPanel, BoxLayout.Y_AXIS));
        productListPanel.setBackground(ALMOND);

        JScrollPane scrollPane = new JScrollPane(productListPanel);
        scrollPane.setBorder(BorderFactory.createLineBorder(RAISIN_BLACK, 2));
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Painel inferior
        JPanel bottomPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        bottomPanel.setBackground(ALMOND);

        bottomPanel.add(new JLabel("Valor Total:", SwingConstants.RIGHT));
        totalLabel = new JLabel("R$ 0.00");
        totalLabel.setFont(new Font("Arial", Font.BOLD, 18));
        bottomPanel.add(totalLabel);

        bottomPanel.add(new JLabel("Valor Pago:", SwingConstants.RIGHT));
        valorPagoField = new JTextField();
        valorPagoField.setFont(new Font("Arial", Font.PLAIN, 16));
        bottomPanel.add(valorPagoField);

        bottomPanel.add(new JLabel("Troco:", SwingConstants.RIGHT));
        trocoLabel = new JLabel("R$ 0.00");
        trocoLabel.setFont(new Font("Arial", Font.BOLD, 18));
        bottomPanel.add(trocoLabel);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        // Botões
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        actionPanel.setBackground(ALMOND);

        btnMais = criarBotao("+", MELON, BURNT_SIENNA);
        btnMais.addActionListener(e -> adicionarLinhaProduto());

        btnCalcularTroco = criarBotao("Calcular Troco", MELON, BURNT_SIENNA);
        btnCalcularTroco.addActionListener(e -> calcularTroco());

        btnSalvar = criarBotao("Salvar", MELON, BURNT_SIENNA);
        btnSalvar.addActionListener(e -> salvarVenda());

        btnCancelar = criarBotao("Cancelar", MELON, BURNT_SIENNA);
        btnCancelar.addActionListener(e -> dispose());

        actionPanel.add(btnMais);
        actionPanel.add(btnCalcularTroco);
        actionPanel.add(btnSalvar);
        actionPanel.add(btnCancelar);

        mainPanel.add(actionPanel, BorderLayout.NORTH);
        add(mainPanel);
        setVisible(true);

        adicionarLinhaProduto();
    }

    private JButton criarBotao(String texto, Color corNormal, Color corHover) {
        JButton botao = new JButton(texto);
        botao.setFont(new Font("Arial", Font.PLAIN, 16));
        botao.setBackground(corNormal);
        botao.setForeground(TEXT_COLOR);
        botao.setFocusPainted(false);
        botao.setBorder(BorderFactory.createLineBorder(BURNT_SIENNA));
        botao.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                botao.setBackground(corHover);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                botao.setBackground(corNormal);
            }
        });
        return botao;
    }

    private void adicionarLinhaProduto() {
        JPanel produtoLinha = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        produtoLinha.setBackground(ALMOND);

        JComboBox<String> comboProdutos = new JComboBox<>();
        for (Produto produto : produtoDAO.listarProdutos()) {
            comboProdutos.addItem(produto.getNome());
        }
        JTextField quantidadeField = new JTextField();
        quantidadeField.setPreferredSize(new Dimension(80, 30));
        quantidadeField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent e) {
                calcularTotal();
            }
        });

        JButton btnMenos = criarBotao("-", MELON, BURNT_SIENNA);
        btnMenos.setPreferredSize(new Dimension(50, 30));
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
            JComboBox<String> combo = (JComboBox<String>) produtoLinha.getComponent(0);
            JTextField quantidadeField = (JTextField) produtoLinha.getComponent(1);
            String nome = (String) combo.getSelectedItem();
            if (nome != null && quantidadeField.getText().matches("\\d+")) {
                Produto produto = produtoDAO.buscarPorNome(nome);
                total += produto.getPreco() * Integer.parseInt(quantidadeField.getText());
            }
        }
        totalLabel.setText(String.format("R$ %.2f", total));
    }

    private void calcularTroco() {
        try {
            double total = Double.parseDouble(totalLabel.getText().replace("R$", "").replace(",", "."));
            double pago = Double.parseDouble(valorPagoField.getText().replace(",", "."));
            double troco = pago - total;

            if (troco >= 0) {
                trocoLabel.setText(String.format("R$ %.2f", troco));
            } else {
                trocoLabel.setText("R$ 0.00");
                JOptionPane.showMessageDialog(this, "Valor pago é menor que o total!", "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Insira um valor válido para o pagamento.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void salvarVenda() {
        JOptionPane.showMessageDialog(this, "Venda registrada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        dispose();
    }
}
