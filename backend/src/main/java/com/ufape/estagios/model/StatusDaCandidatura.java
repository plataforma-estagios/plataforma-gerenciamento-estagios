package com.ufape.estagios.model;

public enum StatusDaCandidatura {
	SUBMETIDA("Submetida"),    
    EM_ANALISE("Em análise"),
    ENTREVISTA ("Entrevista"),
	PROXIMA_ETAPA("Próxima etapa"),
	APROVADA ("Aprovada"),
    REPROVADA ("Reprovada"),    
    CANCELADA ("Cancelada");
	
	private String legenda;

	private StatusDaCandidatura(String legenda) {
		this.legenda = legenda;
	}
	
	public String getLegenda() {
		return this.legenda;
	}
}
