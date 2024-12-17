package view;

import dao.ProdutoDAO;
import model.Produto;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CadastroProdutoFrame extends JFrame {

    public CadastroProdutoFrame() {
        // Configurações da janela
        setTitle("Cadastrar Produto");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(5, 2, 10, 10));

        // Componentes do formulário
        JLabel lblNome = new JLabel("Nome:");
        JTextField txtNome = new JTextField();

        JLabel lblPreco = new JLabel("Preço:");
        JTextField txtPreco = new JTextField();

        JLabel lblQuantidade = new JLabel("Quantidade:");
        JTextField txtQuantidade = new JTextField();

        JButton btnSalvar = new JButton("Salvar");
        JButton btnCancelar = new JButton("Cancelar");

        // Adicionar componentes à janela
        add(lblNome);
        add(txtNome);
        add(lblPreco);
        add(txtPreco);
        add(lblQuantidade);
        add(txtQuantidade);
        add(btnSalvar);
        add(btnCancelar);

        // Ação do botão "Salvar"
        btnSalvar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String nome = txtNome.getText();
                    double preco = Double.parseDouble(txtPreco.getText());
                    int quantidade = Integer.parseInt(txtQuantidade.getText());

                    Produto produto = new Produto(nome, preco, quantidade);
                    ProdutoDAO.cadastrarProduto(produto);

                    JOptionPane.showMessageDialog(CadastroProdutoFrame.this,
                            "Produto cadastrado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);

                    // Limpar campos
                    txtNome.setText("");
                    txtPreco.setText("");
                    txtQuantidade.setText("");
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(CadastroProdutoFrame.this,
                            "Por favor, insira valores válidos nos campos.", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Ação do botão "Cancelar"
        btnCancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Fecha a janela
            }
        });

        setVisible(true); // Exibe a janela
    }
}