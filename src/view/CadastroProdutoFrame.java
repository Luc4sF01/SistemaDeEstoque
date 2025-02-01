package view;

import dao.ProdutoDAO;
import model.Produto;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;

public class CadastroProdutoFrame extends JFrame {

    public CadastroProdutoFrame() {
        setTitle("Cadastrar Produto");
        setSize(450, 360);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Criando painel principal
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.decode("#F7F7F7"));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridwidth = GridBagConstraints.REMAINDER;

        // Título estilizado
        JLabel titleLabel = new JLabel("Cadastro de Produto", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.decode("#5A47AB"));
        panel.add(titleLabel, gbc);

        // Criando campos estilizados
        JTextField txtNome = criarCampoTexto("Nome do Produto");
        JPanel precoPanel = criarPainelPreco();
        JTextField txtQuantidade = criarCampoTexto("Quantidade");

        panel.add(txtNome, gbc);
        panel.add(precoPanel, gbc);
        panel.add(txtQuantidade, gbc);

        // Criando botões
        JButton btnSalvar = criarBotao("Salvar", "#5A47AB", "#483C9B");
        JButton btnCancelar = criarBotao("Cancelar", "#E74C3C", "#C0392B");

        // Ação do botão Salvar
        btnSalvar.addActionListener(e -> {
            try {
                String nome = txtNome.getText().trim();
                double preco = converterPreco(((JTextField) precoPanel.getComponent(1)).getText());
                int quantidade = Integer.parseInt(txtQuantidade.getText().trim());

                Produto produto = new Produto(nome, preco, quantidade);
                ProdutoDAO.cadastrarProduto(produto);

                JOptionPane.showMessageDialog(this, "Produto cadastrado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Por favor, insira valores válidos!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnCancelar.addActionListener(e -> dispose());

        // Criando painel para os botões
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setBackground(Color.decode("#F7F7F7"));
        buttonPanel.add(btnSalvar);
        buttonPanel.add(Box.createHorizontalStrut(20));
        buttonPanel.add(btnCancelar);

        panel.add(buttonPanel, gbc);

        add(panel);
        setVisible(true);
    }

    // Método para criar campos de texto bonitos
    private JTextField criarCampoTexto(String placeholder) {
        JTextField campo = new JTextField(20);
        campo.setFont(new Font("SansSerif", Font.PLAIN, 16));
        campo.setForeground(Color.decode("#333333"));
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.decode("#CCCCCC"), 1, true),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        campo.setBackground(Color.decode("#FFFFFF"));
        campo.setText(placeholder);

        campo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (campo.getText().equals(placeholder)) {
                    campo.setText("");
                }
            }

            public void focusLost(java.awt.event.FocusEvent evt) {
                if (campo.getText().isEmpty()) {
                    campo.setText(placeholder);
                }
            }
        });

        return campo;
    }

    // Método para criar o painel de preço com "R$" fixo
    private JPanel criarPainelPreco() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.decode("#CCCCCC"), 1, true),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        JLabel lblPrefixo = new JLabel("R$ ");
        lblPrefixo.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblPrefixo.setForeground(Color.decode("#333333"));

        JTextField campoPreco = new JTextField();
        campoPreco.setFont(new Font("SansSerif", Font.PLAIN, 16));
        campoPreco.setForeground(Color.decode("#333333"));
        campoPreco.setBorder(null);
        campoPreco.setBackground(Color.decode("#FFFFFF"));
        campoPreco.setHorizontalAlignment(JTextField.LEFT);

        panel.add(lblPrefixo, BorderLayout.WEST);
        panel.add(campoPreco, BorderLayout.CENTER);

        return panel;
    }

    // Método para converter o valor do campo de preço corretamente
    private double converterPreco(String texto) throws NumberFormatException {
        texto = texto.replace(",", ".").trim();
        return Double.parseDouble(texto);
    }

    // Método para criar botões bonitos com hover
    private JButton criarBotao(String texto, String corPrincipal, String corHover) {
        JButton botao = new JButton(texto);
        botao.setFont(new Font("SansSerif", Font.BOLD, 16));
        botao.setForeground(Color.WHITE);
        botao.setBackground(Color.decode(corPrincipal));
        botao.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.decode("#333333"), 1, true),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
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
