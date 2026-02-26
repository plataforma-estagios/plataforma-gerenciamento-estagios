package com.ufape.estagios.model;

public enum StatusDaVaga {
	EM_ABERTO("Em Aberto"),
	FECHADA("Fechada"),
	CANCELADA("Cancelada"),
	ENCERRADA("Encerrada");
	
	private String status;
	
	StatusDaVaga(String status){
		this.status = status;
	}

	public String getStatus() {
		return status;
	}
}
