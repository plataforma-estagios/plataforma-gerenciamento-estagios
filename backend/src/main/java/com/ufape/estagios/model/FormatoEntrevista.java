package com.ufape.estagios.model;

public enum FormatoEntrevista {
    ONLINE("Online"),
    PRESENCIAL("Presencial");

    private String descricao;

    FormatoEntrevista(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}