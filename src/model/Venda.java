package model;

public class Venda {
    private int id;
    private String produtoNome;
    private int quantidade;
    private double total;
    private String data;

    // Construtor
    public Venda(int id, String produtoNome, int quantidade, double total, String data) {
        this.id = id;
        this.produtoNome = produtoNome;
        this.quantidade = quantidade;
        this.total = total;
        this.data = data;
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProdutoNome() {
        return produtoNome;
    }

    public void setProdutoNome(String produtoNome) {
        this.produtoNome = produtoNome;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
