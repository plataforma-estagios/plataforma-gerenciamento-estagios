package com.ufape.estagios.model;

public enum TipoNotificacao {
    SUCESSO("Sucesso"),
    FALHA("Falha"),
    INFORMATIVO("Informativo");

    private String descricao;

    TipoNotificacao(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}