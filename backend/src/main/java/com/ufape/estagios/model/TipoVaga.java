package com.ufape.estagios.model;

public enum TipoVaga {
    ESTAGIO("Estágio"),
    TRAINEE("Trainee"),
    EMPREGO("Emprego");

    private String descricao;

    TipoVaga(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}