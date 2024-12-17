package view;

import dao.DatabaseConnection;
import dao.ProdutoDAO;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    private JPanel mainPanel;
    private JLabel titleLabel;
    private boolean isDarkTheme = true;

    public MainFrame() {
        // Configurações da janela principal
        setTitle("Sistema de Estoque");
        setSize(900, 700); // Define um tamanho inicial
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null); // Centraliza a janela na tela
        setMinimumSize(new Dimension(900, 700)); // Define um tamanho mínimo para a janela

        // Painel principal
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        mainPanel.setBackground(new Color(45, 45, 45));

        // Menu superior
        criarMenuSuperior();

        // Título centralizado
        titleLabel = new JLabel("<html><center>Sistema de Estoque<br><span style='font-size:16px;'>Gerencie seu inventário com eficiência</span></center></html>", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0)); // Espaçamento extra
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Painel para os botões
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 0, 20, 0); // Espaçamento vertical entre os botões
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = GridBagConstraints.REMAINDER; // Para colocar cada botão em uma linha separada

        // Criação dos botões
        JButton btnCadastrar = criarBotao("Cadastrar Produto", "/icons/cadastrar produto.png");
        JButton btnListar = criarBotao("Listar Produtos", "/icons/listar produtos.png");
        JButton btnRegistrarVenda = criarBotao("Registrar Venda", "/icons/resgistar venda.png");
        JButton btnRelatorio = criarBotao("Relatório de Vendas", "/icons/relatorio-de-negocios.png");
        JButton btnSair = criarBotao("Sair", "/icons/sair.png");

        // Adicionar os botões ao painel
        buttonPanel.add(btnCadastrar, gbc);
        buttonPanel.add(btnListar, gbc);
        buttonPanel.add(btnRegistrarVenda, gbc);
        buttonPanel.add(btnRelatorio, gbc);
        buttonPanel.add(btnSair, gbc);

        // Adicionar o painel dos botões ao painel principal
        mainPanel.add(buttonPanel, BorderLayout.CENTER);

        // Rodapé
        JLabel footerLabel = new JLabel("<html><center>Desenvolvido por Lucas - Versão 1.2<br><span style='font-size:12px;'>© 2024 Todos os direitos reservados</span></center></html>", SwingConstants.CENTER);
        footerLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        footerLabel.setForeground(Color.LIGHT_GRAY);
        footerLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0)); // Espaçamento do rodapé
        mainPanel.add(footerLabel, BorderLayout.SOUTH);

        // Adicionar o painel principal à janela
        add(mainPanel);

        // Ações dos Botões
        btnCadastrar.addActionListener(e -> new CadastroProdutoFrame());
        btnListar.addActionListener(e -> new ListagemProdutosFrame());
        btnRegistrarVenda.addActionListener(e -> new RegistrarVendaFrame());
        btnRelatorio.addActionListener(e -> new RelatorioVendasFrame());
        btnSair.addActionListener(e -> System.exit(0));

        setVisible(true);
    }

    // Método para criar o menu superior
    private void criarMenuSuperior() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menuArquivo = new JMenu("Arquivo");
        JMenu menuAjuda = new JMenu("Ajuda");

        JMenuItem itemImportarCSV = new JMenuItem("Importar CSV");
        itemImportarCSV.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Selecione o arquivo CSV");

            int resultado = fileChooser.showOpenDialog(this);
            if (resultado == JFileChooser.APPROVE_OPTION) {
                String caminhoArquivo = fileChooser.getSelectedFile().getAbsolutePath();
                ProdutoDAO.importarCSV(caminhoArquivo);
                JOptionPane.showMessageDialog(this, "Arquivo CSV importado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        JMenuItem itemAlternarTema = new JMenuItem("Alternar Tema");
        itemAlternarTema.addActionListener(e -> toggleTheme());

        JMenuItem itemSair = new JMenuItem("Sair");
        itemSair.addActionListener(e -> System.exit(0));

        JMenuItem itemSobre = new JMenuItem("Sobre");
        itemSobre.addActionListener(e -> JOptionPane.showMessageDialog(this, "Sistema de Estoque v1.2\nDesenvolvido por Lucas\n© 2024"));

        menuArquivo.add(itemImportarCSV);
        menuArquivo.add(itemAlternarTema);
        menuArquivo.add(itemSair);
        menuAjuda.add(itemSobre);

        menuBar.add(menuArquivo);
        menuBar.add(menuAjuda);

        setJMenuBar(menuBar);
    }

    // Método para alternar entre tema claro e escuro
    private void toggleTheme() {
        isDarkTheme = !isDarkTheme;
        if (isDarkTheme) {
            mainPanel.setBackground(new Color(45, 45, 45));
            titleLabel.setForeground(Color.WHITE);
        } else {
            mainPanel.setBackground(new Color(245, 245, 245));
            titleLabel.setForeground(Color.BLACK);
        }
        SwingUtilities.updateComponentTreeUI(this);
    }

    // Criação de Botões com Estilo
    private JButton criarBotao(String texto, String iconePath) {
        JButton botao = new JButton(texto);
        botao.setFont(new Font("Arial", Font.PLAIN, 24));
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource(iconePath));
            Image scaledIcon = icon.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
            botao.setIcon(new ImageIcon(scaledIcon));
        } catch (Exception e) {
            System.err.println("Erro ao carregar ícone: " + iconePath);
        }
        botao.setHorizontalAlignment(SwingConstants.LEFT);
        botao.setFocusPainted(false);
        botao.setBackground(isDarkTheme ? new Color(70, 73, 75) : new Color(220, 220, 220));
        botao.setForeground(isDarkTheme ? Color.WHITE : Color.BLACK);
        botao.setPreferredSize(new Dimension(400, 80));
        botao.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(90, 93, 95)),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        return botao;
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatDarculaLaf());
        } catch (Exception e) {
            System.err.println("Erro ao aplicar o Look and Feel: " + e.getMessage());
        }

        DatabaseConnection.createTable();
        new MainFrame();
    }
}
