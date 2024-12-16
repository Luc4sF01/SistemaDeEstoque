package model;

public class Venda {
    private int id;
    private String produtoNome;
    private int quantidade;
    private double valorTotal;
    private String dataVenda;

    // Construtor completo
    public Venda(int id, String produtoNome, int quantidade, double valorTotal, String dataVenda) {
        this.id = id;
        this.produtoNome = produtoNome;
        this.quantidade = quantidade;
        this.valorTotal = valorTotal;
        this.dataVenda = dataVenda;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getProdutoNome() {
        return produtoNome;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public double getValorTotal() {
        return valorTotal;
    }

    public String getDataVenda() {
        return dataVenda;
    }

    // Setters (caso necessário)
    public void setId(int id) {
        this.id = id;
    }

    public void setProdutoNome(String produtoNome) {
        this.produtoNome = produtoNome;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public void setValorTotal(double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public void setDataVenda(String dataVenda) {
        this.dataVenda = dataVenda;
    }
}
