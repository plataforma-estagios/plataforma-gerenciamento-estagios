package com.ufape.estagios.model;

public enum StatusDaCandidatura {
	SUBMETIDA("Submetida", "Sua candidatura foi submetida.", TipoNotificacao.INFORMATIVO),    
    EM_ANALISE("Em análise", "A Empresa está analisando o seu currículo.", TipoNotificacao.INFORMATIVO),
    ENTREVISTA ("Entrevista", "Sua entrevista foi agendada.", TipoNotificacao.INFORMATIVO),
	PROXIMA_ETAPA("Próxima etapa", "Parabéns! Você passou para a próxima vaga do processo seletivo.", TipoNotificacao.SUCESSO),
	APROVADA ("Aprovada", "Parabéns! Você foi APROVADO(A).", TipoNotificacao.SUCESSO),
    REPROVADA ("Reprovada", "Infelizmente o seu perfil não seguiu adiante na seleção.", TipoNotificacao.FALHA),    
    CANCELADA ("Cancelada", "Infelizmente a sua candidatura foi cancelada.", TipoNotificacao.FALHA);
	
	private String legenda;
	private String mensagemDeNotificacao;
	private TipoNotificacao tipoNotificacao;

	private StatusDaCandidatura(String legenda, String mensagemDeNotificacao, TipoNotificacao tipoNotificacao) {
		this.legenda = legenda;
		this.mensagemDeNotificacao = mensagemDeNotificacao;
		this.tipoNotificacao = tipoNotificacao;
	}
	
	public String getLegenda() {
		return this.legenda;
	}

	public String getMensagemDeNotificacao() {
		return mensagemDeNotificacao;
	}

	public TipoNotificacao getTipoNotificacao() {
		return tipoNotificacao;
	}
}
