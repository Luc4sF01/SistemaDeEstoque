package view;

import dao.DatabaseConnection;
import dao.ProdutoDAO;
import com.formdev.flatlaf.FlatDarculaLaf;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.text.DecimalFormat;
import java.util.Random;

public class MainFrame extends JFrame {

    // Labels do dashboard (tiles) para atualização dos dados
    private JLabel lblTotalProdutos;
    private JLabel lblEstoqueBaixo;
    private JLabel lblTotalVendas;
    private JLabel lblVendasHoje;

    public MainFrame() {
        // Configurações principais da janela
        setTitle("InnovaStock - Gestão Inovadora de Estoque");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Cria e define a barra de menu
        setJMenuBar(criarMenuBar());

        // Cabeçalho com título e slogan
        add(criarPainelCabecalho(), BorderLayout.NORTH);

        // Sidebar (barra lateral) com botões de navegação
        add(criarSidebar(), BorderLayout.WEST);

        // Painel central com o dashboard
        add(criarPainelDashboard(), BorderLayout.CENTER);

        // Rodapé com créditos
        add(criarRodape(), BorderLayout.SOUTH);

        setVisible(true);
    }

    /**
     * Cria a barra de menu superior com os itens "Arquivo" e "Ajuda".
     */
    private JMenuBar criarMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(Color.WHITE);

        // Menu Arquivo
        JMenu menuArquivo = new JMenu("Arquivo");
        JMenuItem miImportarCSV = new JMenuItem("Importar CSV");
        miImportarCSV.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Selecione o arquivo CSV");
            if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                File arquivoSelecionado = fileChooser.getSelectedFile();
                ProdutoDAO.importarCSV(arquivoSelecionado.getAbsolutePath());
                JOptionPane.showMessageDialog(this, "CSV importado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        JMenuItem miSair = new JMenuItem("Sair");
        miSair.addActionListener(e -> System.exit(0));
        menuArquivo.add(miImportarCSV);
        menuArquivo.add(miSair);

        // Menu Ajuda
        JMenu menuAjuda = new JMenu("Ajuda");
        JMenuItem miSobre = new JMenuItem("Sobre");
        miSobre.addActionListener(e ->
                JOptionPane.showMessageDialog(this, "InnovaStock v1.0\nDesenvolvido por Lucas\n© 2024", "Sobre", JOptionPane.INFORMATION_MESSAGE));
        menuAjuda.add(miSobre);

        menuBar.add(menuArquivo);
        menuBar.add(menuAjuda);
        return menuBar;
    }

    /**
     * Cria o cabeçalho com o nome do sistema e um slogan.
     */
    private JPanel criarPainelCabecalho() {
        JPanel cabecalho = new JPanel(new BorderLayout());
        cabecalho.setBackground(new Color(52, 152, 219));
        cabecalho.setPreferredSize(new Dimension(getWidth(), 100));

        JLabel titulo = new JLabel("InnovaStock", SwingConstants.CENTER);
        titulo.setForeground(Color.WHITE);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 36));

        JLabel slogan = new JLabel("Gestão Inovadora de Estoque", SwingConstants.CENTER);
        slogan.setForeground(Color.WHITE);
        slogan.setFont(new Font("SansSerif", Font.PLAIN, 18));

        cabecalho.add(titulo, BorderLayout.CENTER);
        cabecalho.add(slogan, BorderLayout.SOUTH);
        return cabecalho;
    }

    /**
     * Cria a sidebar (barra lateral) com os botões de navegação.
     */
    private JPanel criarSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setBackground(new Color(44, 62, 80));
        sidebar.setPreferredSize(new Dimension(250, getHeight()));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        // Botões de navegação
        JButton btnCadastrarProduto = criarBotaoSidebar("Cadastrar Produto");
        btnCadastrarProduto.addActionListener(e -> new CadastroProdutoFrame());

        JButton btnListarProdutos = criarBotaoSidebar("Listar Produtos");
        btnListarProdutos.addActionListener(e -> new ListagemProdutosFrame());

        JButton btnRegistrarVenda = criarBotaoSidebar("Registrar Venda");
        btnRegistrarVenda.addActionListener(e -> new RegistrarVendaFrame());

        JButton btnRelatorioVendas = criarBotaoSidebar("Relatório de Vendas");
        btnRelatorioVendas.addActionListener(e -> new RelatorioVendasFrame());

        JButton btnAtualizarDashboard = criarBotaoSidebar("Atualizar Dashboard");
        btnAtualizarDashboard.addActionListener(e -> atualizarDashboard());

        JButton btnSair = criarBotaoSidebar("Sair");
        btnSair.addActionListener(e -> System.exit(0));

        // Adiciona os botões com espaçamentos
        sidebar.add(btnCadastrarProduto);
        sidebar.add(Box.createVerticalStrut(15));
        sidebar.add(btnListarProdutos);
        sidebar.add(Box.createVerticalStrut(15));
        sidebar.add(btnRegistrarVenda);
        sidebar.add(Box.createVerticalStrut(15));
        sidebar.add(btnRelatorioVendas);
        sidebar.add(Box.createVerticalStrut(15));
        sidebar.add(btnAtualizarDashboard);
        sidebar.add(Box.createVerticalGlue());
        sidebar.add(btnSair);

        return sidebar;
    }

    /**
     * Cria um botão estilizado para a sidebar.
     */
    private JButton criarBotaoSidebar(String texto) {
        JButton botao = new JButton(texto);
        botao.setAlignmentX(Component.CENTER_ALIGNMENT);
        botao.setMaximumSize(new Dimension(220, 40));
        botao.setFont(new Font("SansSerif", Font.PLAIN, 16));
        botao.setForeground(Color.WHITE);
        botao.setBackground(new Color(52, 73, 94));
        botao.setFocusPainted(false);
        botao.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        // Efeito de hover
        botao.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                botao.setBackground(new Color(41, 128, 185));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                botao.setBackground(new Color(52, 73, 94));
            }
        });
        return botao;
    }

    /**
     * Cria o painel central do dashboard com tiles informativos.
     */
    private JPanel criarPainelDashboard() {
        JPanel dashboard = new JPanel(new BorderLayout());
        dashboard.setBackground(Color.WHITE);
        dashboard.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblDashboard = new JLabel("Dashboard", SwingConstants.LEFT);
        lblDashboard.setFont(new Font("SansSerif", Font.BOLD, 24));
        lblDashboard.setForeground(new Color(33, 33, 33));
        dashboard.add(lblDashboard, BorderLayout.NORTH);

        // Painel dos tiles em grid 2x2
        JPanel tilesPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        tilesPanel.setBackground(Color.WHITE);

        Color corTile = new Color(236, 240, 241);

        // Tile: Total de Produtos
        JPanel tileTotalProdutos = criarTile(corTile, "Total de Produtos");
        lblTotalProdutos = new JLabel("0");
        configurarValorTile(lblTotalProdutos);
        tileTotalProdutos.add(lblTotalProdutos);

        // Tile: Estoque Baixo
        JPanel tileEstoqueBaixo = criarTile(corTile, "Estoque Baixo");
        lblEstoqueBaixo = new JLabel("0");
        configurarValorTile(lblEstoqueBaixo);
        tileEstoqueBaixo.add(lblEstoqueBaixo);

        // Tile: Total de Vendas
        JPanel tileTotalVendas = criarTile(corTile, "Total de Vendas");
        lblTotalVendas = new JLabel("R$ 0,00");
        configurarValorTile(lblTotalVendas);
        tileTotalVendas.add(lblTotalVendas);

        // Tile: Vendas Hoje
        JPanel tileVendasHoje = criarTile(corTile, "Vendas Hoje");
        lblVendasHoje = new JLabel("0");
        configurarValorTile(lblVendasHoje);
        tileVendasHoje.add(lblVendasHoje);

        tilesPanel.add(tileTotalProdutos);
        tilesPanel.add(tileEstoqueBaixo);
        tilesPanel.add(tileTotalVendas);
        tilesPanel.add(tileVendasHoje);

        dashboard.add(tilesPanel, BorderLayout.CENTER);

        // Botão de atualização dos dados do dashboard
        JPanel painelRefresh = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        painelRefresh.setBackground(Color.WHITE);
        JButton btnRefresh = new JButton("Atualizar Dados");
        btnRefresh.setFont(new Font("SansSerif", Font.PLAIN, 16));
        btnRefresh.setBackground(new Color(52, 152, 219));
        btnRefresh.setForeground(Color.WHITE);
        btnRefresh.setFocusPainted(false);
        btnRefresh.addActionListener(e -> atualizarDashboard());
        painelRefresh.add(btnRefresh);
        dashboard.add(painelRefresh, BorderLayout.SOUTH);

        // Inicializa os dados do dashboard (valores simulados)
        atualizarDashboard();

        return dashboard;
    }

    /**
     * Cria um painel (tile) para o dashboard, com título e estilo definidos.
     */
    private JPanel criarTile(Color cor, String tituloTile) {
        JPanel tile = new JPanel();
        tile.setBackground(cor);
        tile.setLayout(new BoxLayout(tile, BoxLayout.Y_AXIS));
        tile.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titulo = new JLabel(tituloTile);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 18));
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        tile.add(titulo);
        tile.add(Box.createVerticalStrut(10));
        return tile;
    }

    /**
     * Configura o estilo dos valores exibidos nos tiles.
     */
    private void configurarValorTile(JLabel label) {
        label.setFont(new Font("SansSerif", Font.PLAIN, 32));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        label.setForeground(new Color(33, 33, 33));
    }

    /**
     * Cria o rodapé com os créditos do sistema.
     */
    private JPanel criarRodape() {
        JPanel rodape = new JPanel(new FlowLayout(FlowLayout.CENTER));
        rodape.setBackground(new Color(189, 195, 199));
        JLabel lblRodape = new JLabel("Desenvolvido por Lucas - Versão 1.2 © 2024");
        lblRodape.setFont(new Font("SansSerif", Font.ITALIC, 12));
        rodape.add(lblRodape);
        return rodape;
    }

    /**
     * Atualiza os dados do dashboard com valores simulados.
     * (Substitua por chamadas reais ao banco, se disponível.)
     */
    private void atualizarDashboard() {
        Random random = new Random();
        int totalProdutos = random.nextInt(800) + 200;          // 200 a 999 produtos
        int estoqueBaixo = random.nextInt(50);                    // 0 a 49 itens em estoque baixo
        double totalVendas = random.nextDouble() * 100000;        // Valor em vendas
        int vendasHoje = random.nextInt(100);                     // 0 a 99 vendas no dia

        DecimalFormat df = new DecimalFormat("#,###.00");

        lblTotalProdutos.setText(String.valueOf(totalProdutos));
        lblEstoqueBaixo.setText(String.valueOf(estoqueBaixo));
        lblTotalVendas.setText("R$ " + df.format(totalVendas));
        lblVendasHoje.setText(String.valueOf(vendasHoje));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(new FlatDarculaLaf());
            } catch (Exception e) {
                e.printStackTrace();
            }
            DatabaseConnection.createTable();
            new MainFrame();
        });
    }
}
