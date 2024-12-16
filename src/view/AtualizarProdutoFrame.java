package view;

import dao.ProdutoDAO;
import model.Produto;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AtualizarProdutoFrame extends JFrame {

    public AtualizarProdutoFrame(Produto produtoSelecionado) {
        // Configurações da janela
        setTitle("Atualizar Produto");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(5, 2, 10, 10));

        // Componentes do formulário
        JLabel lblNome = new JLabel("Nome:");
        JTextField txtNome = new JTextField(produtoSelecionado.getNome());

        JLabel lblPreco = new JLabel("Preço:");
        JTextField txtPreco = new JTextField(String.valueOf(produtoSelecionado.getPreco()));

        JLabel lblQuantidade = new JLabel("Quantidade:");
        JTextField txtQuantidade = new JTextField(String.valueOf(produtoSelecionado.getQuantidade()));

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
                    // Captura os dados do formulário
                    String nome = txtNome.getText();
                    double preco = Double.parseDouble(txtPreco.getText());
                    int quantidade = Integer.parseInt(txtQuantidade.getText());

                    // Atualiza os dados do produto selecionado
                    produtoSelecionado.setNome(nome);
                    produtoSelecionado.setPreco(preco);
                    produtoSelecionado.setQuantidade(quantidade);

                    // Chama o método para atualizar no banco de dados
                    ProdutoDAO.atualizarProduto(produtoSelecionado);

                    JOptionPane.showMessageDialog(AtualizarProdutoFrame.this,
                            "Produto atualizado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);

                    dispose(); // Fecha a janela após salvar
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(AtualizarProdutoFrame.this,
                            "Por favor, insira valores válidos nos campos.", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Ação do botão "Cancelar"
        btnCancelar.addActionListener(e -> dispose());

        setVisible(true); // Exibe a janela
    }
}
