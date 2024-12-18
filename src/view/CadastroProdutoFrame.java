package view;

import dao.ProdutoDAO;
import model.Produto;

import javax.swing.*;
import java.awt.*;

public class CadastroProdutoFrame extends JFrame {

    // 🌈 Paleta de cores personalizada
    private final Color MELON = new Color(255, 188, 181);
    private final Color BURNT_SIENNA = new Color(201, 125, 96);
    private final Color TEXT_COLOR = new Color(51, 51, 51);
    private final Color CAPUT_MORTUUM = new Color(99, 55, 44);
    private final Color ALMOND = new Color(242, 229, 215);

    public CadastroProdutoFrame() {
        // Configurações da janela
        setTitle("Cadastrar Produto");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);

        // Título
        JLabel titleLabel = new JLabel("Cadastro de Produto", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(TEXT_COLOR);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(titleLabel, BorderLayout.NORTH);

        // Painel central (formulário)
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        formPanel.setBackground(ALMOND);
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JLabel lblNome = new JLabel("Nome:");
        lblNome.setForeground(TEXT_COLOR);
        JTextField txtNome = new JTextField();

        JLabel lblPreco = new JLabel("Preço:");
        lblPreco.setForeground(TEXT_COLOR);
        JTextField txtPreco = new JTextField();

        JLabel lblQuantidade = new JLabel("Quantidade:");
        lblQuantidade.setForeground(TEXT_COLOR);
        JTextField txtQuantidade = new JTextField();

        formPanel.add(lblNome);
        formPanel.add(txtNome);
        formPanel.add(lblPreco);
        formPanel.add(txtPreco);
        formPanel.add(lblQuantidade);
        formPanel.add(txtQuantidade);
        add(formPanel, BorderLayout.CENTER);

        // Painel de botões
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(ALMOND);

        JButton btnSalvar = criarBotao("Salvar", MELON);
        JButton btnCancelar = criarBotao("Cancelar", CAPUT_MORTUUM);

        buttonPanel.add(btnSalvar);
        buttonPanel.add(btnCancelar);
        add(buttonPanel, BorderLayout.SOUTH);

        // Ação do botão "Salvar"
        btnSalvar.addActionListener(e -> {
            try {
                String nome = txtNome.getText().trim();
                double preco = Double.parseDouble(txtPreco.getText().trim());
                int quantidade = Integer.parseInt(txtQuantidade.getText().trim());

                Produto produto = new Produto(nome, preco, quantidade);
                ProdutoDAO.cadastrarProduto(produto);

                JOptionPane.showMessageDialog(this,
                        "Produto cadastrado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                        "Por favor, insira valores válidos para preço e quantidade.", "Erro", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Erro ao cadastrar produto: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Ação do botão "Cancelar"
        btnCancelar.addActionListener(e -> dispose());

        setVisible(true);
    }

    // Criação de botões estilizados
    private JButton criarBotao(String texto, Color corFundo) {
        JButton botao = new JButton(texto);
        botao.setFont(new Font("Arial", Font.BOLD, 18));
        botao.setForeground(Color.WHITE);
        botao.setBackground(corFundo);
        botao.setFocusPainted(false);
        botao.setPreferredSize(new Dimension(120, 40));
        botao.setBorder(BorderFactory.createLineBorder(BURNT_SIENNA));

        // Efeito hover
        botao.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                botao.setBackground(BURNT_SIENNA);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                botao.setBackground(corFundo);
            }
        });
        return botao;
    }
}
