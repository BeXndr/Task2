package com.example.task2;

public class Tarefa {
     
    private int id;
    private String descricao;
    private String data_criacao;
    private String data_prevista;
    private String data_encerramento;
    private String situacao;

    public Tarefa(int id, String descricao, String data_criacao, String data_prevista, String data_encerramento, String situacao) {
        this.id = id;
        this.descricao = descricao;
        this.data_criacao = data_criacao;
        this.data_prevista = data_prevista;
        this.data_encerramento = data_encerramento;
        this.situacao = situacao;
    }

    public Tarefa() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getData_criacao() {
        return data_criacao;
    }

    public void setData_criacao(String data_criacao) {
        this.data_criacao = data_criacao;
    }

    public String getData_prevista() {
        return data_prevista;
    }

    public void setData_prevista(String data_prevista) {
        this.data_prevista = data_prevista;
    }

    public String getData_encerramento() {
        return data_encerramento;
    }

    public void setData_encerramento(String data_encerramento) {
        this.data_encerramento = data_encerramento;
    }

    public String getSituacao() {
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    
}