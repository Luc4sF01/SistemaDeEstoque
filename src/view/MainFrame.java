package view;

import dao.DatabaseConnection;
import dao.ProdutoDAO;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    private JPanel mainPanel;
    private JLabel titleLabel;
    private boolean isDarkTheme = true;

    // 🎨 Paleta de cores personalizada
    private final Color RAISIN_BLACK = new Color(38, 35, 34);
    private final Color CAPUT_MORTUUM = new Color(99, 55, 44);
    private final Color BURNT_SIENNA = new Color(201, 125, 96);
    private final Color MELON = new Color(255, 188, 181);
    private final Color ALMOND = new Color(242, 229, 215);
    private final Color TEXT_COLOR = new Color(51, 51, 51); // Cinza escuro para textos

    public MainFrame() {
        // Configurações da janela principal
        setTitle("Sistema de Estoque");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(900, 700));

        // Painel principal
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        mainPanel.setBackground(ALMOND);

        // Menu superior
        criarMenuSuperior();

        // Título centralizado
        titleLabel = new JLabel("<html><center>Sistema de Estoque<br><span style='font-size:16px;'>Gerencie seu inventário com eficiência</span></center></html>", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setForeground(TEXT_COLOR);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Painel dos botões
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 0, 20, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = GridBagConstraints.REMAINDER;

        // Botões estilizados
        JButton btnCadastrar = criarBotao("Cadastrar Produto");
        JButton btnListar = criarBotao("Listar Produtos");
        JButton btnRegistrarVenda = criarBotao("Registrar Venda");
        JButton btnRelatorio = criarBotao("Relatório de Vendas");
        JButton btnSair = criarBotao("Sair");

        buttonPanel.add(btnCadastrar, gbc);
        buttonPanel.add(btnListar, gbc);
        buttonPanel.add(btnRegistrarVenda, gbc);
        buttonPanel.add(btnRelatorio, gbc);
        buttonPanel.add(btnSair, gbc);

        mainPanel.add(buttonPanel, BorderLayout.CENTER);

        // Rodapé
        JLabel footerLabel = new JLabel("<html><center>Desenvolvido por Lucas - Versão 1.2<br><span style='font-size:12px;'>© 2024 Todos os direitos reservados</span></center></html>", SwingConstants.CENTER);
        footerLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        footerLabel.setForeground(TEXT_COLOR);
        footerLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        mainPanel.add(footerLabel, BorderLayout.SOUTH);

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
        menuBar.setBackground(MELON);

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
            mainPanel.setBackground(RAISIN_BLACK);
            titleLabel.setForeground(ALMOND);
        } else {
            mainPanel.setBackground(ALMOND);
            titleLabel.setForeground(TEXT_COLOR);
        }
        SwingUtilities.updateComponentTreeUI(this);
    }

    // Criação de Botões com Estilo
    private JButton criarBotao(String texto) {
        JButton botao = new JButton(texto);
        botao.setFont(new Font("Arial", Font.PLAIN, 20));
        botao.setForeground(TEXT_COLOR);
        botao.setBackground(MELON);
        botao.setFocusPainted(false);
        botao.setPreferredSize(new Dimension(300, 50));
        botao.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BURNT_SIENNA),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));

        // Efeito de hover no botão
        botao.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                botao.setBackground(BURNT_SIENNA);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                botao.setBackground(MELON);
            }
        });

        return botao;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatDarculaLaf());
            } catch (Exception e) {
                e.printStackTrace();
            }
            DatabaseConnection.createTable();
            new MainFrame();
        });
    }
}
