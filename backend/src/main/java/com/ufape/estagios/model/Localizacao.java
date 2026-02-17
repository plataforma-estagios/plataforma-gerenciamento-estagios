package com.ufape.estagios.model;

public enum Localizacao {
    PRESENCIAL("Presencial"),
    REMOTO("Remoto"),
    HIBRIDO("Híbrido");

    private String descricao;

    Localizacao(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}